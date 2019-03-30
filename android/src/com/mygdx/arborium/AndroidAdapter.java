package com.mygdx.arborium;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class AndroidAdapter implements NotificationHandler
{
    private Context context;

    public AndroidAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public void showNotification(String title, String text)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            Notification.Builder mBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(text);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(1, mBuilder.build());
            }
        }
    }
}
