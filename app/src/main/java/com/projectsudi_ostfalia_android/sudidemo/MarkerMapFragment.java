package com.projectsudi_ostfalia_android.sudidemo;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerMapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;

    private static final String mLogTag = "SudiDEMO MarkerActivity";
    private static List<Marker> markerList = new ArrayList<>();
    private static int markerCounter = 0;
    Map<String,MapMarker> addedMarkers = new HashMap<>();
    private EditText input;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                //LatLng sydney = new LatLng(-34, 151);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                addEventListener();

            }
        });

        return rootView;
    }

    void addEventListener() {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng));
                marker.setTag(markerCounter);
                marker.setDraggable(true);
                markerList.add(marker);
                markerCounter++;
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                if(!marker.getTag().toString().equals("noEdit")){
                    FragmentManager fm = getActivity().getFragmentManager();
                    MarkerMapFragment.MyDialogFragment dialogFragment = new MarkerMapFragment.MyDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", (int) marker.getTag());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(fm, "Pfad Dialogbox");
                }else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setPosition(marker.getPosition());
            }
        });
    }

    public void addmarker(MapMarker marker) {
        if(addedMarkers.containsKey(marker.id)){
            CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.position).zoom(googleMap.getCameraPosition().zoom).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }else {
            MarkerOptions options = new MarkerOptions();
            Log.d(mLogTag,marker.titel);
            Log.d(mLogTag,marker.beschreibung);
            Log.d(mLogTag,marker.position.toString());

            options.title(marker.titel);
            options.snippet(marker.beschreibung);
            options.position(marker.position);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            addedMarkers.put(marker.id,marker);

            googleMap.addMarker(options).setTag("noEdit");
        }
    }

    public static class MyDialogFragment extends DialogFragment {

        private Marker marker;
        private EditText editTextTitel;
        private EditText editTextBeschreibung;
        private AlertDialog alertDialog;
        private TextView textViewTitel;
        private TextView textViewBeschreibung;
        private TextView textViewLatLng;
        private Button buttonOk;
        private Button buttonLoeschen;
        private Switch switchBearbeiten;

        @Override
        public void onStart() {
            super.onStart();
            textViewTitel = (TextView) getDialog().findViewById(R.id.dialog_marker_titel);
            textViewBeschreibung = (TextView) getDialog().findViewById(R.id.dialog_marker_beschreibung);
            textViewLatLng = (TextView) getDialog().findViewById(R.id.dialog_marker_LatLng);

            buttonOk = (Button)getDialog().findViewById(R.id.button_dialogMarkerOk);
            buttonLoeschen = (Button)getDialog().findViewById(R.id.button_dialogMarkerLöschen);
            buttonOk.setVisibility(View.GONE);
            buttonLoeschen.setVisibility(View.GONE);

            editTextTitel = (EditText) getDialog().findViewById(R.id.dialog_titel_eingeben);
            editTextBeschreibung = (EditText) getDialog().findViewById(R.id.dialog_beschreibung_eingeben);
            editTextBeschreibung.setVisibility(View.GONE);
            editTextTitel.setVisibility(View.GONE);


            textViewTitel.setText(marker.getTitle());
            textViewBeschreibung.setText(marker.getSnippet());
            textViewLatLng.setText(marker.getPosition().toString());

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

            switchBearbeiten = getDialog().findViewById(R.id.dialog_marker_switch);


            if(textViewTitel.getText().toString().isEmpty()) textViewTitel.setVisibility(View.GONE);
            if(textViewBeschreibung.getText().toString().isEmpty()) textViewBeschreibung.setVisibility(View.GONE);
            switchBearbeiten.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextBeschreibung.getVisibility() == View.GONE) {
                        editTextTitel.setVisibility(View.VISIBLE);
                        editTextBeschreibung.setVisibility(View.VISIBLE);
                        buttonOk.setVisibility(View.VISIBLE);
                        buttonLoeschen.setVisibility(View.VISIBLE);
                        if(!(textViewTitel.getText().toString().isEmpty() && textViewBeschreibung.getText().toString().isEmpty())){
                            editTextTitel.setText(textViewTitel.getText());
                            editTextBeschreibung.setText(textViewBeschreibung.getText());
                        }
                    } else {
                        editTextTitel.setVisibility(View.GONE);
                        editTextBeschreibung.setVisibility(View.GONE);
                        buttonOk.setVisibility(View.GONE);
                        buttonLoeschen.setVisibility(View.GONE);
                    }
                }
            });
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String titelInput = editTextTitel.getText().toString();
                    String beschreibungInput = editTextBeschreibung.getText().toString();
                    if(!(titelInput.equals("") && beschreibungInput.equals("")))
                    {
                        textViewTitel.setText(titelInput);
                        textViewBeschreibung.setText(beschreibungInput);
                        marker.setTitle(titelInput);
                        marker.setSnippet(beschreibungInput);
                    }
                    editTextTitel.setVisibility(View.GONE);
                    editTextBeschreibung.setVisibility(View.GONE);
                    buttonOk.setVisibility(View.GONE);
                    buttonLoeschen.setVisibility(View.GONE);
                    textViewTitel.setVisibility(View.VISIBLE);
                    textViewBeschreibung.setVisibility(View.VISIBLE);
                    switchBearbeiten.setChecked(false);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            });
            buttonLoeschen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    marker.remove();
                    alertDialog.dismiss();
                }
            });
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_marker, null));

            Bundle bundle = getArguments();
            marker = markerList.get(bundle.getInt("position"));

            builder.setPositiveButton("Hochladen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String titel = marker.getTitle();
                    String beschreibung = marker.getSnippet();
                    LatLng position = marker.getPosition();
                    CreateMarker(titel,beschreibung, position);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    marker.setTag("noEdit");
                    marker.setDraggable(false);
                    dismiss();
                }

            });
            builder.setNeutralButton("Speichern", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    marker.setTitle(textViewTitel.getText().toString());
                    marker.setSnippet(textViewBeschreibung.getText().toString());
                    dismiss();
                }
            });

            builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

            alertDialog = builder.create();
            return alertDialog;
        }

        private void CreateMarker(final String titel, final String beschreibung, final LatLng latLng) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(" https://sudirest20181030020815.azurewebsites.net/api/marker/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);


                        JSONObject jsonParam1 = new JSONObject();
                        jsonParam1.put("titel", titel);
                        jsonParam1.put("beschreibung", beschreibung);
                        jsonParam1.put("lng", latLng.longitude);
                        jsonParam1.put("lat", latLng.latitude);




                        //DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //StringEntity entity = new StringEntity(jsonParam1.toString(), "UTF-8");
                        //byte[] array = entity.toString().getBytes("UTF-8");
                        //os.writeBytes(URLEncoder.encode(jsonParam1.toString(), "UTF-8"));
                        //os.write(array,0,array.length);
                        //os.writeUTF(URLEncoder.encode(jsonParam1.toString(), "UTF-8"));
                        //os.writeChars(URLEncoder.encode(jsonParam1.toString(), "UTF-8"));
                        //os.writeUTF();
                        //Log.d(mLogTag, jsonParam1.toString());

                        //os.flush();
                        //os.close();

                        HttpClient httpClient = new DefaultHttpClient(); //Use this instead

                        try {

                            HttpPost request = new HttpPost(url.toURI());
                            StringEntity params =new StringEntity(jsonParam1.toString(),"UTF-8");
                            request.addHeader("content-type", "application/json");
                            request.setEntity(params);
                            HttpResponse response = httpClient.execute(request);

                            //handle response here...

                        }catch (Exception ex) {

                            //handle exception here

                        } finally {
                            //Deprecated
                            httpClient.getConnectionManager().shutdown();
                        }

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG", conn.getResponseMessage());

                        conn.disconnect();
                    } catch (Exception e) {
                        Log.d(mLogTag,e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
