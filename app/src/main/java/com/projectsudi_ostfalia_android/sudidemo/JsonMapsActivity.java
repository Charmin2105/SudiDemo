package com.projectsudi_ostfalia_android.sudidemo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JsonMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        try{
            GeoJsonLayer jsonLayer = new GeoJsonLayer(mMap, R.raw.mapjson,getApplicationContext());
            jsonLayer.addLayerToMap();



            for (GeoJsonFeature feature : jsonLayer.getFeatures()) {
                // Check if the magnitude property exists
                if (feature.getProperty("title") != null) {
                    String title = feature.getProperty("title");


                    // Create a new point style
                    GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();

                    // Set options for the point style
                    pointStyle.setTitle("Title: " + title);
                    pointStyle.setSnippet("Geometry: "+ feature.getGeometry().getGeometryObject());
                    Log.d("MYTAG",feature.getGeometry().getGeometryObject().toString());

                    // Assign the point style to the feature
                    feature.setPointStyle(pointStyle);

                    GeoJsonLineStringStyle lineStyle = new GeoJsonLineStringStyle();
                    lineStyle.setColor(Color.CYAN);
                    feature.setLineStringStyle(lineStyle);

                    GeoJsonPolygonStyle polyStyle = new GeoJsonPolygonStyle();
                    polyStyle.setFillColor(Color.argb(128,255,151,255));
                    feature.setPolygonStyle(polyStyle);
                }
            }


            jsonLayer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                @Override
                public void onFeatureClick(Feature feature) {


                    Toast.makeText(JsonMapsActivity.this,
                            "Feature clicked: " + feature.getProperty("title"),
                            Toast.LENGTH_LONG).show();
                }

            });

        }catch (IOException ioEx){

        }catch (JSONException jsonEx){

        }
    }
}
