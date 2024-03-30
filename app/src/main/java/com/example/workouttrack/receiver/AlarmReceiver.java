package com.example.workouttrack.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.workouttrack.ui.MainActivity;
import com.example.workouttrack.R;
import com.example.workouttrack.WorkoutManager;
import com.example.workouttrack.models.WorkoutItem;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    public final static String NOTIFICATION_CHANNEL_ID = "workoutSchedule";
    private final static String NOTIFICATION_CHANNEL_NAME = "workoutItem";
    public final static int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<WorkoutItem> workoutSchedule = WorkoutManager.getInstance().getWorkoutSchedule();
        if (workoutSchedule != null) {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // DAY_OF_WEEK starting from Sunday
            int adjustedDayOfWeek = (dayOfWeek + 5) % 7;

            if (adjustedDayOfWeek >= 0 && adjustedDayOfWeek < workoutSchedule.size()) {
                WorkoutItem todayWorkout = workoutSchedule.get(adjustedDayOfWeek);
                String workoutTitle = todayWorkout.getTitle();
                String workoutContent = todayWorkout.getContent();
                createNotification(context,workoutTitle,workoutContent);
            } else {
                // AdjustedDayOfWeek is out of bounds
                Log.d("AlarmReceiver","Today is not working day");
            }
        }else {
            Log.d("AlarmReceiver","Alarm received but schedule is null");
        }
        WorkoutManager.getInstance().setNextTrigger(context);
    }

    /**
     *
     * @param context
     * @param title
     * @param content
     */
    private void createNotification(Context context, String title, String content) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

