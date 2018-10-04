package com.stepCone.sridatta.CollegeAssistant;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by HP-PC on 24-01-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent=new Intent(this,SplashScreen.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
       /* NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentTitle("New Notification");
        builder.setContentText("hello");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_menu_gallery);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());*/


    }
}
