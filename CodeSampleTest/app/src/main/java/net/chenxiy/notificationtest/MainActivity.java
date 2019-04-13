package net.chenxiy.notificationtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button_notify;
    private Button button_cancel;
    private Button button_update;
    private Button button_job;
    private static final String CHANNEL_ID="primary channel";
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";
    private NotificationReceiver mReceiver = new NotificationReceiver();
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_notify=findViewById(R.id.notify);
        button_cancel=findViewById(R.id.cancel);
        button_update=findViewById(R.id.update);
        button_job=findViewById(R.id.jbscheduler);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationManager.cancel(NOTIFICATION_ID);
            }
        });

        button_update.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

                Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);
                notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(androidImage)
                        .setBigContentTitle("Notification Updated!"));
                mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());

            }
        });

        button_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),NotificationScheduler.class);
                startActivity(intent);
            }
        });
        createNotificationChannel();
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

    }

    public void startCanvas(View view) {

        Intent intent=new Intent(view.getContext(),Canvas.class);
        startActivity(intent);
    }


    public class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {


            Toast.makeText(context,"hahah",Toast.LENGTH_LONG).show();
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.notificationimage, "Update Notification", updatePendingIntent)
                .addAction(R.drawable.notificationimage, "Update Notification", updatePendingIntent)
                .addAction(R.drawable.notificationimage, "Update Notification", updatePendingIntent)
                .setSmallIcon(R.drawable.notificationimage);
        return notifyBuilder;



    };

    public void createNotificationChannel(){
        mNotificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"test",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("description");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }


    }

    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
