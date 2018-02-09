package com.prospektdev.ezrotation;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

abstract class NotificationHelper {
    private static final String NOTIFICATION_CHANNEL = "Notification channel";
    private static final String SECONDARY_CHANNEL = "second";

    @NonNull
    static Notification createForegroundNotification(@NonNull Context context,
                                                     @NonNull String title,
                                                     @NonNull String body) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChanel(context);
            return getNotificationOreo(context, title, body).build();
        } else {
            return getNotification(context, title, body).build();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    @NonNull
    private static Notification.Builder getNotificationOreo(
            @NonNull Context context,
            @NonNull String title,
            @NonNull String body) {
        return new Notification.Builder(context, SECONDARY_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private static NotificationCompat.Builder getNotification(@NonNull Context context,
                                                              @NonNull String title,
                                                              @NonNull String body) {
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setOngoing(true);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChanel(@NonNull Context context) {
        final NotificationChannel channel = new NotificationChannel(SECONDARY_CHANNEL,
                NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getNotificationManager(context).createNotificationChannel(channel);
    }

    private static NotificationManager getNotificationManager(@NonNull Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
