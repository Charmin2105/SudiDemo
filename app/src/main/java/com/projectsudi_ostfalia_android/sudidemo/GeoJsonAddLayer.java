package com.projectsudi_ostfalia_android.sudidemo;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GeoJsonAddLayer extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton floatButton;
    private final static String mLogTag = "SudiDEMO GeoJsonDemo";
    private String mUrl;
    private RouteManager routeManager;
    boolean[] checkedItems;


    public Activity getActivity(){return this;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_json_add_layer);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        init();

    }

    private void init() {



        routeManager = new RouteManager(this);
        routeManager.Setup();

        checkedItems = new boolean[routeManager.Routes().length];

        floatButton = findViewById(R.id.floatButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fm = getActivity().getFragmentManager();
                GeoJsonAddLayer.MyDialogFragment dialogFragment = new GeoJsonAddLayer.MyDialogFragment();
                dialogFragment.show(fm, "Pfad Dialogbox");*/

                //region AlertDialog
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(GeoJsonAddLayer.this);
                mBuilder.setTitle("Verfügbare Routen");
                mBuilder.setMultiChoiceItems(routeManager.RouteReadable(), checkedItems , new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            checkedItems[position] = true;

                        }else{
                            checkedItems[position] = false;
                            if(routeManager.Routes()[position].layer != null){
                                routeManager.Routes()[position].layer.removeLayerFromMap();
                            }
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        for (int i = 0; i < checkedItems.length; i++){
                            if(checkedItems[i]){
                                if(routeManager.Routes()[i].downloaded){
                                    Log.d(mLogTag,"Already Downloaded: " + routeManager.Routes()[i].titel);
                                    routeManager.Routes()[i].layer.addLayerToMap();
                                }else {
                                    Log.d(mLogTag,"Downloading new route: "+ routeManager.Routes()[i].titel);
                                    retrieveFileFromUrl(routeManager.Routes()[i]);
                                }
                            }
                        }
                    }
                });
                /*mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++ ){
                            checkedItems[i] = false;
                            for (boolean b: checkedItems
                                 ) {
                                b = false;
                            }
                            for (Route r: routeManager.Routes()
                                 ) {
                                if(r.downloaded){
                                    r.layer.removeLayerFromMap();
                                }

                            }
                        }

                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
                //endregion AlertDialog
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap = googleMap;



            mMap.setMyLocationEnabled(true);


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


    }
    private void retrieveFileFromUrl(Route s) {
        new GeoJsonAddLayer.DownloadGeoJsonFile().execute(s);
    }


    private class DownloadGeoJsonFile extends AsyncTask<Route, Void, Route> {

        @Override
        protected Route doInBackground(Route... params) {
            try {
                // Open a stream from the URL
                InputStream stream = new URL(params[0].url).openStream();

                String line;
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                while ((line = reader.readLine()) != null) {
                    // Read and save each line of the stream
                    result.append(line);
                }

                // Close the stream
                reader.close();
                stream.close();


                routeManager.Routes()[params[0].id].layer = new GeoJsonLayer(mMap, new JSONObject(result.toString()));
                routeManager.Routes()[params[0].id].downloaded = true;

                return routeManager.Routes()[params[0].id];
            } catch (IOException e) {
                Log.e(mLogTag, "GeoJSON file could not be read");
            } catch (JSONException e) {
                Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Route route) {
            if (route.layer != null) {
                addGeoJsonLayerToMap(route.layer);
            }
        }

    }

    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {

        //addColorsToMarkers(layer);
        layer.addLayerToMap();

        //getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(31.4118,-103.5355)));
        // Demonstrate receiving features via GeoJsonLayer clicks.
        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                Toast.makeText(GeoJsonAddLayer.this,
                        "Feature clicked: " + feature.getProperty("title"),
                        Toast.LENGTH_SHORT).show();
            }

        });
    }


    // Not in use
    //Dialog Fragment das angezeigt wird sobald der FloatingButton gedrückt wurde
    //Hier kann der Benutzer eine URL eingeben um eine KML Datei hinzuzufügen
    /*public static class MyDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialoge_route, null));


            final GeoJsonAddLayer jsonMapsURL = (GeoJsonAddLayer) getActivity();
            builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText = (EditText) getDialog().findViewById(R.id.dialog_url_eingeben);
                    if (editText != null) {
                        Log.d(mLogTag,editText.getText().toString());
                        ((GeoJsonAddLayer)getActivity()).mUrl = editText.getText().toString();
                        ((GeoJsonAddLayer)getActivity()).retrieveFileFromUrl(((GeoJsonAddLayer)getActivity()).mUrl);
                    } else {
                        // Nicht gefunden
                    }
                }
            });

            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
            return builder.create();
        }
    }*/
}
