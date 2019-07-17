package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_RECIEVE_SMS = 123;
    private static final String MY_PREFS_NAME = "MY_PREFS_NAME";
    TextView car, load;

    private static BroadcastReceiver mListener;

    ConstraintLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recieveMessageRequest();
        car = findViewById(R.id.car);
        load = findViewById(R.id.load);
        background = findViewById(R.id.backgroung);
        Intent service = new Intent(this, MyService.class);
        this.startService(service);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String name = prefs.getString("message", "No name defined");
        if(name.equals("The car is safe")){
            car.setText("The car is safe");
            load.setText("Load Sensor is off");
            background.setBackgroundColor(getResources().getColor(R.color.white));
        }else if(name.equals("The car is not safe")){
            car.setText("The car is not safe");
            load.setText("Load Sensor is on");
            background.setBackgroundColor(getResources().getColor(R.color.red));
        }

        mListener = new BroadcastReceiver() {
            private static final String MY_PREFS_NAME = "MY_PREFS_NAME";

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("TAG", "ACTION_SCREEN_OFF");
                Bundle data = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String Sender = smsMessage.getDisplayOriginatingAddress();
                    String EmailFrom = smsMessage.getEmailFrom();
                    String EmailBody = smsMessage.getEmailBody();
                    String Displaymessagebody = smsMessage.getDisplayMessageBody();
                    String Time = smsMessage.getTimestampMillis() + "";
                    String Message = smsMessage.getMessageBody();




                    if(Sender.equals("+923165765464") || Sender.equals("03165765464")){
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("message", Message);
                        editor.apply();

                        if(Message.equals("The car is safe")){
                            car.setText("The car is safe");
                            load.setText("Load Sensor is off");
                            background.setBackgroundColor(getResources().getColor(R.color.green));
                        }else if(Message.equals("The car is not safe")){
                            car.setText("The car is not safe");
                            load.setText("Load Sensor is on");
                            background.setBackgroundColor(getResources().getColor(R.color.red));
                        }


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



    protected void recieveMessageRequest() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        PERMISSION_REQUEST_RECIEVE_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_RECIEVE_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Read Messages Permission granted");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    private void showNotification(Context context) {

        ShowNotificationIntentService.showStatusBarIcon(context);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mListener);
        mListener = null;
    }

}
