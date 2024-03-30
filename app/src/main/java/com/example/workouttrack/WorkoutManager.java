package com.example.workouttrack;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.workouttrack.models.WorkoutItem;
import com.example.workouttrack.receiver.AlarmReceiver;
import com.example.workouttrack.ui.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkoutManager {
    public final static int DEFAULT_TRIGGER_HOUR = 13;
    public final static int DEFAULT_TRIGGER_MINUTE = 0;

    private static WorkoutManager instance;

    private ArrayList<WorkoutItem> workoutSchedule;

    private WorkoutManager() {
        // Initialize workout schedule data
        workoutSchedule = new ArrayList<>();
        workoutSchedule.add(new WorkoutItem("Monday", "Cardio and Core Strength",
                "•30 minutes of cardio\n" +
                        "•15 minutes of core exercises\n" +
                        "•15 minutes of stretching and cool down"));
        workoutSchedule.add(new WorkoutItem("Tuesday", "Upper Body Strength Training",
                "•20 minutes of upper body weightlifting\n" +
                        "•20 minutes of body weight exercises\n" +
                        "•20 minutes of stretching and cool down"));
        workoutSchedule.add(new WorkoutItem("Wednesday", "Yoga and Flexibility",
                "•60 minutes of yoga, focusing on flexibility and balance"));
        workoutSchedule.add(new WorkoutItem("Thursday", "Lower Body Strength Training",
                "•20 minutes of lower body weightlifting\n" +
                        "•20 minutes of body weight exercises\n" +
                        "•20 minutes of stretching and cool down"));
        workoutSchedule.add(new WorkoutItem("Friday", "Cardio Intervals",
                "•30 minutes of cardio\n" +
                        "•15 minutes of core strengthening exercises\n" +
                        "•15 minutes of stretching and cool down"));
    }

    public static synchronized WorkoutManager getInstance() {
        if (instance == null) {
            instance = new WorkoutManager();
        }
        return instance;
    }

    public ArrayList<WorkoutItem> getWorkoutSchedule() {
        return workoutSchedule;
    }

    /**
     *
     * @param context
     */
    public void setNextTrigger(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.class.getSimpleName(),Context.MODE_PRIVATE);
        int savedHour = sharedPreferences.getInt("hour", DEFAULT_TRIGGER_HOUR);
        int savedMinute = sharedPreferences.getInt("minute", DEFAULT_TRIGGER_MINUTE);
        int savedNotificationIndex = sharedPreferences.getInt("notificationIndex", 1);
        setTrigger(context,savedHour,savedMinute,1,savedNotificationIndex);
    }

    /**
     *
     * @param context
     * @param hour
     * @param minute
     * @param addDay
     * @param selectIndex
     */
    @SuppressLint("ScheduleExactAlarm")
    public void setTrigger(Context context, int hour, int minute, int addDay, int selectIndex) {
        Calendar nextTriggerTime = Calendar.getInstance();
        nextTriggerTime.set(Calendar.HOUR_OF_DAY, hour);
        nextTriggerTime.set(Calendar.MINUTE, minute);
        nextTriggerTime.set(Calendar.SECOND, 0);

        //Transfer the index to prior time
        int priorTime = (selectIndex + 1) * 5;
        nextTriggerTime.add(Calendar.DAY_OF_YEAR, addDay);
        nextTriggerTime.add(Calendar.MINUTE, -priorTime);

        Intent newIntent = new Intent(context, AlarmReceiver.class);
        Log.d("WorkoutManager","hour:" + hour + " minute:" + minute + " prior Time:" + priorTime);

        PendingIntent newPendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTriggerTime.getTimeInMillis(), newPendingIntent);
    }
}
