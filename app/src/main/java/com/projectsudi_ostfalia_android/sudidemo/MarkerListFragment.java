package com.projectsudi_ostfalia_android.sudidemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarkerListFragment extends Fragment {

    private String[] lv_arr = {};
    ArrayAdapter<String> adapter;
    List<MapMarker> markers;
    private ListView listView;
    ItemClicked mCallback;
    private MarkerManager markerManager;
    private final String mLogTag = "SudiDEMO MarkerListFrag";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_markerlist, container, false);

        listView = view.findViewById(R.id.listviewMarkerList);
        //lv_arr = getResources().getStringArray(R.array.test);
        markerManager = MarkerManager.getInstance();
        markerManager.Setup(getActivity());

        lv_arr = markerManager.MarkerTitelArray();

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, lv_arr);
        listView.setAdapter(adapter);

        //markers = new ArrayList<>();


        addEventlistener();

        return view;
    }

    void addEventlistener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(mLogTag,"Test 1");
                someMethod(markerManager.getMarker(position));
                Log.d(mLogTag,"Test 2");
            }
        });
    }

    public interface ItemClicked{
        public void sendMarker(MapMarker marker);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ItemClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TextClicked");
        }
    }

    public void someMethod(MapMarker marker){
        mCallback.sendMarker(marker);

    }

    @Override
    public void onDetach() {
        mCallback = null; // => avoid leaking, thanks @Deepscorn
        super.onDetach();
    }

}
