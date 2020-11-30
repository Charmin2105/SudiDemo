package com.projectsudi_ostfalia_android.sudidemo;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SplitActivity extends Activity implements MarkerListFragment.ItemClicked{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
    }

    @Override
    public void sendMarker(MapMarker marker) {

        MarkerMapFragment fragment = (MarkerMapFragment) getFragmentManager().findFragmentById(R.id.omega);
        fragment.addmarker(marker);
    }
}
