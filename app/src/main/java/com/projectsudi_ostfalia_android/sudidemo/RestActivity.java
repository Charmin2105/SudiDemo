package com.projectsudi_ostfalia_android.sudidemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestActivity extends AppCompatActivity {

    private Button senden;
    private View.OnClickListener demoOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        senden = (Button) findViewById(R.id.button_restSenden);

        demoOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(v);
            }
        };

        senden.setOnClickListener(demoOnClickListener);
    }

    void startDemo(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button_restSenden:
                Senden();
                break;
        }
    }

    private void Senden()
    {
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


                    JSONObject coor = new JSONObject();
                    coor.put("lng", "10.7061767578125");
                    coor.put("lat", "53.19616119954287");

                    JSONObject coor2 = new JSONObject();
                    coor2.put("lng", "10.696563720703125");
                    coor2.put("lat", "53.033781297849195");

                    JSONObject coor3 = new JSONObject();
                    coor3.put("lng", "10.518035888671875");
                    coor3.put("lat", "52.97428123025625");

                    JSONObject coor4 = new JSONObject();
                    coor4.put("lng", "10.369720458984375");
                    coor4.put("lat", "53.11628428290746");

                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(coor);
                    jsonArray.put(coor2);
                    jsonArray.put(coor3);
                    jsonArray.put(coor4);


                    JSONObject jsonParam1 = new JSONObject();
                    jsonParam1.put("titel", "Dennis");
                    jsonParam1.put("beschreibung", "Welzer");
                    jsonParam1.put("coordinates", jsonArray);


                    Log.d("TESTBLA", jsonParam1.toString());

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

}
