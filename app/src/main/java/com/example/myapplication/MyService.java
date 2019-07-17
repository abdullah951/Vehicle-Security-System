package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyService extends Service
{
    private static BroadcastReceiver mListener;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(mListener);
        mListener = null;
    }

    private void registerScreenOffReceiver() {
        mListener = new BroadcastReceiver() {
            private static final String MY_PREFS_NAME = "MY_PREFS_NAME";

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("TAG", "ACTION_SCREEN_OFF");
                Bundle data = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String message = "Sender : " + smsMessage.getDisplayOriginatingAddress()
                            + "Email From: " + smsMessage.getEmailFrom()
                            + "Email Body: " + smsMessage.getEmailBody()
                            + "Display message body: " + smsMessage.getDisplayMessageBody()
                            + "Time in millisecond: " + smsMessage.getTimestampMillis()
                            + "Message: " + smsMessage.getMessageBody();
                    String Sender = smsMessage.getDisplayOriginatingAddress();
                    String EmailFrom = smsMessage.getEmailFrom();
                    String EmailBody = smsMessage.getEmailBody();
                    String Displaymessagebody = smsMessage.getDisplayMessageBody();
                    String Time = smsMessage.getTimestampMillis() + "";
                    String Message = smsMessage.getMessageBody();




                    if(Sender.equals("+923165765464") || Sender.equals("03165765464")) {
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("message", message);
                        editor.apply();
                        if(Message.equals("The car is not safe")){
                            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                                    new Intent(context, MainActivity.class), 0);

                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(context)
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setContentTitle("Vehicle Security System")
                                            .setContentText("The car is not safe");
                            mBuilder.setContentIntent(contentIntent);
                            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                            mBuilder.setAutoCancel(true);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(1, mBuilder.build());
                        }
                    }


                }
            }
        };
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mListener, filter);
    }
}