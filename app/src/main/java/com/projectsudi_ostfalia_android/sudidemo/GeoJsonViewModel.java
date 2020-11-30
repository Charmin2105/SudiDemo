package com.projectsudi_ostfalia_android.sudidemo;

import android.arch.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonViewModel extends ViewModel {

    private List<LatLng> mLatLngList = null;

    public List<LatLng> getLatLngList()
    {
        if(mLatLngList == null)
        {
            mLatLngList = new ArrayList<LatLng>();
        }
        return mLatLngList;
    }

    public void addPosition(LatLng latLng)
    {
        mLatLngList.add(latLng);
    }
}

