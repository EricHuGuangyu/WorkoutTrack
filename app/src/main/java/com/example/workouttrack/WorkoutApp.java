package com.example.workouttrack;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.example.workouttrack.service.WorkoutService;

public class WorkoutApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Start the service
        Intent serviceIntent = new Intent(this, WorkoutService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }
}
