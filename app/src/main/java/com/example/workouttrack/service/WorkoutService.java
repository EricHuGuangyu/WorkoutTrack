package com.example.workouttrack.service;

import static com.example.workouttrack.receiver.AlarmReceiver.NOTIFICATION_CHANNEL_ID;
import static com.example.workouttrack.receiver.AlarmReceiver.NOTIFICATION_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.workouttrack.R;
import com.example.workouttrack.WorkoutManager;
import com.example.workouttrack.ui.MainActivity;

public class WorkoutService extends Service {
    public static Handler serviceHandler;
    private int hour;
    private int minute;
    private int selectIndex;
    SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create a notification channel
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Workout Schedule Channel",
                NotificationManager.IMPORTANCE_DEFAULT);

        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
        int savedHour = sharedPreferences.getInt("hour", WorkoutManager.DEFAULT_TRIGGER_HOUR);
        int savedMinute = sharedPreferences.getInt("minute", WorkoutManager.DEFAULT_TRIGGER_MINUTE);
        int savedNotificationIndex = sharedPreferences.getInt("notificationIndex", 1);
        WorkoutManager.getInstance().setTrigger(this,savedHour,savedMinute,0,savedNotificationIndex);

        return START_STICKY;
    }

    /**
     *
     * @return
     */
    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Workout schedule")
                .setContentText("Persist Notification")
                .setPriority(Notification.PRIORITY_DEFAULT);
        return builder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        serviceHandler = new Handler(message -> {
            switch (message.what) {
                case MainActivity.MSG_UPDATE_INTERVAL:
                     hour = message.arg1;
                     minute = message.arg2;
                     selectIndex = (int)message.obj;
                     WorkoutManager.getInstance().setTrigger(this,hour,minute,0,selectIndex);
                    break;
            }
            return false;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}