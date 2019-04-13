package net.chenxiy.notificationtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.ThreadLocalRandom;

public class NotificationJobService extends JobService {
    public NotificationJobService() {
    }
    NotificationManager mNotifyManager;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";


    @Override
    public boolean onStartJob(JobParameters params) {
        //Create the notification channel
        createNotificationChannel();

//Set up the notification content intent to launch the app when clicked
//        PendingIntent contentPendingIntent = PendingIntent.getActivity
//                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(getBaseContext(),PRIMARY_CHANNEL_ID)
//                .setContentTitle("Job Service")
//                .setContentText("Your Job is running")
//                .setContentIntent(contentPendingIntent)
//                .setSmallIcon(R.drawable.notificationimage)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setAutoCancel(true);
//
//        mNotifyManager.notify(0,notificationBuilder.build());


        NotificationTask task=new NotificationTask();
        task.doInBackground();
        return true;//return false because the job is finished here
    }

    public class NotificationTask extends AsyncTask<Void,Void,JobParameters>{

        @Override
        protected JobParameters doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PendingIntent contentPendingIntent = PendingIntent.getActivity
                    (getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(getBaseContext(),PRIMARY_CHANNEL_ID)
                    .setContentTitle("Job Service")
                    .setContentText("Your Job is running")
                    .setContentIntent(contentPendingIntent)
                    .setSmallIcon(R.drawable.notificationimage)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);

            mNotifyManager.notify(0,notificationBuilder.build());
            return null;
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            jobFinished(jobParameters,false);//因为onstart里边return的是false，所以这里手动call jobFinished

        }
    }



    @Override
    public boolean onStopJob(JobParameters params) {
        return true;//if job fails, reschedule the job instead of drop
    }


    public void createNotificationChannel() {

        // Define notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Job Service notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifications from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
