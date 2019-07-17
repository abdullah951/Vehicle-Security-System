package com.example.myapplication;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ShowNotificationIntentService extends IntentService {
    private static final String ACTION_SHOW_NOTIFICATION = "my.app.service.action.show";
    private static final String ACTION_HIDE_NOTIFICATION = "my.app.service.action.hide";
    private static final int STATUS_ICON_REQUEST_CODE = 123;
    private static final String START = "my.app.service.action.start";


    public ShowNotificationIntentService() {
        super("ShowNotificationIntentService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(START);
        context.startService(intent);
    }


    public static void startActionShow(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_SHOW_NOTIFICATION);
        context.startService(intent);
    }

    public static void startActionHide(Context context) {
        Intent intent = new Intent(context, ShowNotificationIntentService.class);
        intent.setAction(ACTION_HIDE_NOTIFICATION);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_NOTIFICATION.equals(action)) {
                handleActionShow();
            } else if (ACTION_HIDE_NOTIFICATION.equals(action)) {
                handleActionHide();
            } else if (START.equals(action)) {
                handleStart();
            }
        }
    }

    private void handleStart() {
    }

    private void handleActionShow() {
        showStatusBarIcon(ShowNotificationIntentService.this);
    }

    private void handleActionHide() {
        //hideStatusBarIcon(ShowNotificationIntentService.this);
    }

    public static void showStatusBarIcon(Context ctx) {
        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
