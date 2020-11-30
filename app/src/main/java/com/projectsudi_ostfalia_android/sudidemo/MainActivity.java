package com.projectsudi_ostfalia_android.sudidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

//AIzaSyDDeqQlGjd9Iluemvs7rb8cHmdWfYppNWk
public class MainActivity extends AppCompatActivity {

    private Button demo1, demo2, demo3, demo4, demo5, demo6, demo7, demo8, demo9, demo10,demo11,demo12,demo13, demo14,demo15;
    private View.OnClickListener demoOnClickListener;
    private FirebaseUser mUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonOnClicks();

    }


    void buttonOnClicks(){
        demo1 = (Button) findViewById(R.id.demo1);
        demo2 = (Button) findViewById(R.id.demo2);
        demo3 = (Button) findViewById(R.id.demo3);
        demo4 = (Button) findViewById(R.id.demo4);
        demo5 = (Button) findViewById(R.id.demo5);
        demo6 = (Button) findViewById(R.id.demo6);
        demo7 = (Button) findViewById(R.id.demo7);
        demo8 = (Button) findViewById(R.id.demo8);
        demo9 = (Button) findViewById(R.id.demo9);
        demo10 = (Button) findViewById(R.id.demo10);
        demo11 = (Button) findViewById(R.id.demo11);
        demo12 = (Button) findViewById(R.id.demo12);
        demo13 = (Button) findViewById(R.id.demo13);
        demo14 = (Button) findViewById(R.id.demo14);
        demo15 = (Button) findViewById(R.id.demo15);

        demoOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(v);
            }
        };

        demo1.setOnClickListener(demoOnClickListener);
        demo2.setOnClickListener(demoOnClickListener);
        demo3.setOnClickListener(demoOnClickListener);
        demo4.setOnClickListener(demoOnClickListener);
        demo5.setOnClickListener(demoOnClickListener);
        demo6.setOnClickListener(demoOnClickListener);
        demo7.setOnClickListener(demoOnClickListener);
        demo8.setOnClickListener(demoOnClickListener);
        demo9.setOnClickListener(demoOnClickListener);
        demo10.setOnClickListener(demoOnClickListener);
        demo11.setOnClickListener(demoOnClickListener);
        demo12.setOnClickListener(demoOnClickListener);
        demo13.setOnClickListener(demoOnClickListener);
        demo14.setOnClickListener(demoOnClickListener);
        demo15.setOnClickListener(demoOnClickListener);
    }
    void startDemo(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.demo1:
                intent = new Intent(getBaseContext(), QRScanner.class);
                break;

            case R.id.demo2:
                intent = new Intent(getBaseContext(), KMLActivity.class);
                break;

            case R.id.demo3:
                intent = new Intent(getBaseContext(), AudioActivity.class);
                break;

            case R.id.demo4:
                intent = new Intent(getBaseContext(), NotificationActivity.class);
                break;

            case R.id.demo5:
                intent = new Intent(getBaseContext(), FireBaseLoginActivity.class);
                break;

            case R.id.demo6:
                intent = new Intent(getBaseContext(), GoogleLoginActivity.class);
                break;

            case R.id.demo7:
                intent = new Intent(getBaseContext(), RestActivity.class);
                break;

            case R.id.demo8:
                intent = new Intent(getBaseContext(), GeoJsonAddLayer.class);
                break;

            case R.id.demo9:
                intent = new Intent(getBaseContext(), BookActivity.class);
                break;

            case R.id.demo10:
                intent = new Intent(getBaseContext(), GeoJsonCreateRoute.class);
                break;

            case R.id.demo11:
                intent = new Intent(getBaseContext(), ViewModelActivity.class);
                break;
            case R.id.demo12:
                intent = new Intent(getBaseContext(), OsmActivity.class);
                break;
            case R.id.demo13:
                intent = new Intent(getBaseContext(), VideoActivity.class);
                break;
            case R.id.demo14:
                intent = new Intent(getBaseContext(), SplitActivity.class);
                break;
            case R.id.demo15:
                intent = new Intent(getBaseContext(), CreateFolder.class);
                break;

            default:
                intent = new Intent(getBaseContext(), MainActivity.class);
                break;
        }
        startActivity(intent);
    }
}
