package com.coursehub.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String SUBSCRIBE = "course";
    String NOTIFICATION_CHANNEL_ID = "Rohit_channel";

    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        FirebaseMessaging.getInstance().subscribeToTopic("course");
        final Intent intent = new Intent(this, MainActivity.class);

        long pattern[] = {0, 1000, 500, 1000};
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(remoteMessage.getData().get("message"));
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(pattern);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.setDescription("");
            channel.canBypassDnd();
            NotificationManager nm = this.getSystemService(NotificationManager.class); //error
            assert nm != null;
            nm.createNotificationChannel(channel);

        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setVibrate(pattern)
                .setColor(Color.RED)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(remoteMessage.getData().get("message"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher, 10)
                .setContentIntent(pendingIntent);

        assert notificationManager != null;
        notificationManager.notify(100, builder.build());
    }
}
