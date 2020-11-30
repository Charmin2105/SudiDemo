package com.projectsudi_ostfalia_android.sudidemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RouteManager {
    private Route[] mRoutes;
    private Context mContext;

    private final String mLogTag = "SudiDEMO RouteManager";

    public RouteManager(Context context){
        mContext = context;
    }

    public void Setup(){
        String jsonStrig = getJsonString();
        mRoutes = getRouteArrayFromJsonString(jsonStrig);
    }
    
    public Route[] Routes(){return mRoutes;}
    
    public String[] RouteReadable(){
        
        String[] stringArray = new String[mRoutes.length];

        for (int i = 0; i < mRoutes.length; i++){
            stringArray[i] = mRoutes[i].titel;
        }
        return stringArray;
    }



    private String getJsonString(){
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                String jsonString = new FetchRoutes().execute().get();
                return jsonString;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Route[] getRouteArrayFromJsonString(String s)
    {
        try {
            List<Route> list = new ArrayList<>();
            // Convert the response into a JSON object.

            JSONArray itemsArray = new JSONArray(s);

            // Initialize iterator and results fields.
            int i = 0;
            String titel = null;
            String beschreibung = null;
            String id = null;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (i < itemsArray.length() || (titel == null && beschreibung == null)) {
                // Get the current item information.
                JSONObject jsonObject = itemsArray.getJSONObject(i);
                id  = jsonObject.getString("id");
                titel = jsonObject.getString("titel");
                beschreibung = jsonObject.getString("beschreibung");                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {

                    Log.d(mLogTag,"Id:" + id);
                    Log.d(mLogTag,"Titel:" + titel);
                    Log.d(mLogTag,"Beschreibung:" + beschreibung);
                    Log.d(mLogTag,"Nummer" + i);

                    Route route = new Route();
                    route.url = "https://sudirest20181030020815.azurewebsites.net/api/route/" +id;
                    route.titel = titel;
                    route.beschreibung = beschreibung;
                    route.id = i;
                    list.add(route);
                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            Route[] array = new Route[list.size()];
            return list.toArray(array);

        } catch (Exception e){


            e.printStackTrace();
        }

        return null;
    }
}
