package com.pfe.enginapp.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.pfe.enginapp.App;
import com.pfe.enginapp.Permission;
import com.pfe.enginapp.R;
import com.pfe.enginapp.receivers.NotificationReceiver;
import com.pfe.enginapp.services.AuthenticationService;
import com.pfe.enginapp.services.InterventionAlertService;

public class MainActivity extends AppCompatActivity {

    private TextView textView ;
    private AuthenticationService authenticationService;
    private static final String TAG = "MainActivity";

    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //notificationManager = NotificationManagerCompat.from(this);



        //get the needed permissions for the use of this application.
        Permission permission = new Permission(this);




        //startService(new Intent(MainActivity.this, InterventionAlertService.class));


//        textView = findViewById(R.id.textview);

        /*Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();*/

        authenticationService  = new AuthenticationService(MainActivity.this,AuthenticationService.MAIN_ACTIVITY);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //do the transitiona the animations

        authenticationService.authenticateWithRedirection(authenticationService.DASHBOARD_ACTIVITY);









    }

    @Override
    protected void onStop() {
        super.onStop();

        MainActivity.this.finish();
    }

    public void sendOnChannel1(View view) {


        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/siren");

        String title = "title ";
        String message = "message body";
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)

                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                //.setAutoCancel(true)
                //.setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)

                .build();
        notification.flags |= Notification.FLAG_INSISTENT;
        //notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(1, notification);
    }


}
