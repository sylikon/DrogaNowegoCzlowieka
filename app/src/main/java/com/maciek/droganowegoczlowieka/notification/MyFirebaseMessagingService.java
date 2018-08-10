package com.maciek.droganowegoczlowieka.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.maciek.droganowegoczlowieka.Activities.MainActivity;
import com.maciek.droganowegoczlowieka.R;

import java.net.URLDecoder;

/**
 * Created by Geezy on 10.08.2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String image = remoteMessage.getNotification().getIcon();
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();
        String sound = remoteMessage.getNotification().getSound();

        int id = 0;
        Object obj = remoteMessage.getData().get("id");
        if(obj!=null){
            id=Integer.valueOf(obj.toString());
        }


        this.sendNotification(new NotificationData(title,text, id));
    }

    private void sendNotification(NotificationData notificationData){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{intent},
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;
        try {

            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_list_black)
                    .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
                    .setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(notificationBuilder !=null){
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationData.getId(), notificationBuilder.build());
        }else {
            Log.d(TAG, "cos poszlo nie tak co sie stalo bracia z nami");
        }

    }
}
