package com.projectsudi_ostfalia_android.sudidemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class NotificationActivity extends AppCompatActivity {

    EditText editTextName, editTextTitel, editTextNachricht;

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextTitel = (EditText) findViewById(R.id.editText_titel);
        editTextNachricht = (EditText) findViewById(R.id.editText_nachricht);

        Button b1 = (Button) findViewById(R.id.button_senden);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = editTextName.getText().toString().trim();
                String titel = editTextTitel.getText().toString().trim();
                String nachricht = editTextNachricht.getText().toString().trim();


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //Die If Abfrage ist für Android 8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

                    // Configure the notification channel.
                    notificationChannel.setDescription(name);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                        .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(titel)
                        .setContentText(nachricht);

                notificationManager.notify(NOTIFICATION_ID, builder.build());

            }
        });
    }


}
