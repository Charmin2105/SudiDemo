package com.projectsudi_ostfalia_android.sudidemo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonCreateRoute extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton floatButton;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static final int    DEFAULT_MIN_DISTANCE_CHANGE_FOR_UPDATES =          0;  // 5 m
    private static final int    DEFAULT_MIN_TIME_BW_UPDATES             =   1000;  // 1 sec
    private final static String mLogTag = "GeoJsonDemo";
    private String mRouteTitel;
    private String mRouteBeschreibung;
    private static  List<LatLng> mLatLngList;
    private static GeoJsonViewModel geoJsonViewModel;
    private GeoJsonLayer layer;

    public Activity getActivity(){return this;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_json_create_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        geoJsonViewModel =  ViewModelProviders.of(this).get(GeoJsonViewModel.class);
        mLatLngList = geoJsonViewModel.getLatLngList();

        floatButton = findViewById(R.id.floatButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                GeoJsonCreateRoute.MyDialogFragment dialogFragment = new GeoJsonCreateRoute.MyDialogFragment();
                dialogFragment.show(fm, "Pfad Dialogbox");
            }
        });
        // Location Manager
        mLocationManager = ( LocationManager ) this.getSystemService( LOCATION_SERVICE );
        addEventListener();
        getLocation();
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

            layer = new GeoJsonLayer(mMap,new JSONObject());

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    //Dialog Fragment das angezeigt wird sobald der FloatingButton gedrückt wurde
    //Hier kann der Benutzer eine URL eingeben um eine KML Datei hinzuzufügen
    public static class MyDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_route_create, null));


            final GeoJsonCreateRoute jsonMapsURL = (GeoJsonCreateRoute) getActivity();
            builder.setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editTextTitel = (EditText) getDialog().findViewById(R.id.dialog_titel_eingeben);
                    EditText editTextBeschreibung = (EditText) getDialog().findViewById(R.id.dialog_beschreibung_eingeben);

                        CreateRoute(editTextTitel.getText().toString(), editTextBeschreibung.getText().toString());
                    GeoJsonLineString lineString =  new GeoJsonLineString(mLatLngList);
                    GeoJsonFeature feature = new GeoJsonFeature(lineString,null,null,null);
                    jsonMapsURL.layer.addFeature(feature);
                    jsonMapsURL.layer.addLayerToMap();
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
    }

    private static void CreateRoute(final String titel, final String beschreibung){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(" https://sudirest20181030020815.azurewebsites.net/api/route/");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);


                    mLatLngList = geoJsonViewModel.getLatLngList();


                    JSONArray jsonArray = new JSONArray();
                    for (LatLng latLng: mLatLngList
                         ) {
                        JSONObject coor = new JSONObject();
                        coor.put("lng", latLng.longitude);
                        coor.put("lat", latLng.latitude);
                        jsonArray.put(coor);
                    }


                    JSONObject jsonParam1 = new JSONObject();
                    jsonParam1.put("titel", titel);
                    jsonParam1.put("beschreibung", beschreibung);
                    jsonParam1.put("coordinates", jsonArray);


                    Log.d(mLogTag, jsonParam1.toString());

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam1.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    private void addEventListener() {

        // Listener für onLocation Changed.
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                //Wenn GPS Folgen aktiviert ist, wird die Methode updateDeviceLocation() aufgerufen
                Log.d(mLogTag,"Änderung der Position");
                updateDeviceLocation();



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    //Position wird aktualesiert
    public void updateDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            // Die Letzte Position abfragen und in Variablen speichern
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location currentLocation = (Location) task.getResult();
                        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        moveCamera(latLng, mMap.getCameraPosition().zoom);

                        geoJsonViewModel.addPosition(latLng);

                        //mLatLngList.add(latLng);
                    } else {

                    }
                }
            });

        } catch (SecurityException e) {
        }
    }

    // Methode um die Position der Camera zu verändern
    private void moveCamera(LatLng latLng, float zoom) {

        Log.d(mLogTag, "moveCamera() " + "LAT: " + latLng.latitude + " LNG: " + latLng.longitude);

        //Position und Zoom der Kamera ändern
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    // Methode um den LocationManager zu zu deklarieren.
    private void getLocation( ) {
        // Überprüft die Berechtigung
        if( ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }

        // Registriert den Listener mit dem Standort Manager um Ortsaktualisierungen zu erhalten
        if( mLocationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    DEFAULT_MIN_TIME_BW_UPDATES,
                    DEFAULT_MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    mLocationListener);
    }
}
