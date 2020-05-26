package com.example.takvimuygulamasi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BroadcastReceiver extends android.content.BroadcastReceiver {

    Context context;
    String name, description, sound;
    long[] vib = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        name = intent.getExtras().getString("name");
        description = intent.getExtras().getString("description");
        sound = intent.getStringExtra("ringtone");

        issueNotification(context, intent);
    }

    void issueNotification(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("channel1", "chan",
                    NotificationManager.IMPORTANCE_DEFAULT);
        }

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, "channel1");

        Uri ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (sound != null) {
            ringTone = Uri.parse(intent.getStringExtra("ringtone"));
        }

        Ringtone r = RingtoneManager.getRingtone(context, ringTone);
        r.play();

        notification
                .setSmallIcon(R.mipmap.icon1)
                .setContentTitle(name)
                .setContentText(description)
                .setVibrate(vib)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon2));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance) {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

}


