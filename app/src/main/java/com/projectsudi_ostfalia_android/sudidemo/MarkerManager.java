package com.projectsudi_ostfalia_android.sudidemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MarkerManager {
    private MapMarker[] mMarkers;
    private static MarkerManager instance;
    //private Context mContext;

    private final String mLogTag = "SudiDEMO MarkerManager";

    private MarkerManager(){

    }

    public static synchronized MarkerManager getInstance(){
        if (MarkerManager.instance == null) {
            MarkerManager.instance = new MarkerManager ();
        }
        return MarkerManager.instance;
    }


    public void Setup(Context context){
        String jsonStrig = getJsonString(context);
        mMarkers = getMarkerArrayFromJsonString(jsonStrig);
    }

    public MapMarker getMarker(int id){
        return mMarkers[id];
    }

    public String[] MarkerTitelArray(){

        String[] stringArray = new String[mMarkers.length];

        for (int i = 0; i < mMarkers.length; i++){
            stringArray[i] = mMarkers[i].titel;
        }
        return stringArray;
    }



    private String getJsonString(Context context){
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                String jsonString = new FetchMarkers().execute().get();
                return jsonString;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private MapMarker[] getMarkerArrayFromJsonString(String s)
    {
        try {
            List<MapMarker> list = new ArrayList<>();
            // Convert the response into a JSON object.

            JSONArray itemsArray = new JSONArray(s);

            // Initialize iterator and results fields.
            int i = 0;
            String titel = null;
            String beschreibung = null;
            LatLng position = null;
            String id = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (titel == null && beschreibung == null)) {
                // Get the current item information.
                JSONObject jsonObject = itemsArray.getJSONObject(i);
                id  = jsonObject.getString("id");
                titel = jsonObject.getString("titel");
                beschreibung = jsonObject.getString("beschreibung");
                String lng = jsonObject.getString("lng");
                String lat = jsonObject.getString("lat");
                position = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                // catch if either field is empty and move on.
                try {

                    Log.d(mLogTag,"Id:" + id);
                    Log.d(mLogTag,"Titel:" + titel);
                    Log.d(mLogTag,"Beschreibung:" + beschreibung);
                    Log.d(mLogTag,"Position:" + position.toString());
                    Log.d(mLogTag,"Nummer" + i);

                    MapMarker mapMarker = new MapMarker();
                    mapMarker.id = id;
                    mapMarker.url = "https://sudirest20181030020815.azurewebsites.net/api/marker/" +id;
                    mapMarker.titel = titel;
                    mapMarker.beschreibung = beschreibung;
                    mapMarker.position = position;
                    //mapMarker.id = i;
                    list.add(mapMarker);
                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            MapMarker[] array = new MapMarker[list.size()];
            return list.toArray(array);

        } catch (Exception e){


            e.printStackTrace();
        }

        return null;
    }
}
