package com.example.workouttrack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.workouttrack.R;
import com.example.workouttrack.WorkoutManager;
import com.example.workouttrack.service.WorkoutService;
import com.example.workouttrack.adapter.HorizontalAdapter;
import com.example.workouttrack.models.WorkoutItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int MSG_UPDATE_INTERVAL = 1;
    TimePicker timePicker;
    Spinner notificationSpinner;
    SharedPreferences sharedPreferences;
    private WorkoutManager workoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // Initialize workout schedule data
        workoutManager = WorkoutManager.getInstance();
        ArrayList<WorkoutItem> workoutSchedule = workoutManager.getWorkoutSchedule();

        // Initialize ListView and adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHorizontal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        HorizontalAdapter adapter = new HorizontalAdapter(this,workoutSchedule);
        recyclerView.setAdapter(adapter);

        // Initialize TimePicker and Spinner
        timePicker = findViewById(R.id.timePicker);
        notificationSpinner = findViewById(R.id.notificationSpinner);

        // Set default time from SharedPreferences
        int savedHour = sharedPreferences.getInt("hour", WorkoutManager.DEFAULT_TRIGGER_HOUR);
        int savedMinute = sharedPreferences.getInt("minute", WorkoutManager.DEFAULT_TRIGGER_MINUTE);
        timePicker.setHour(savedHour);
        timePicker.setMinute(savedMinute);

        // Set default notification time from SharedPreferences
        int savedNotificationIndex = sharedPreferences.getInt("notificationIndex", 1);
        notificationSpinner.setSelection(savedNotificationIndex);

        // Set click listener for Save button
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                int selectIndex = notificationSpinner.getSelectedItemPosition();
                saveSettings(hour,minute,selectIndex);
                updateServiceData(hour,minute,selectIndex);
            }
        });
    }

    /**
     *
     * @param hour
     * @param minute
     * @param selectIndex
     */
    private void saveSettings(int hour,int minute,int selectIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.putInt("notificationIndex",selectIndex);
        editor.apply();

        Toast.makeText(this, "Repeat time: " + hour + ":" + minute +
                "\nPrior time: " + getResources().getStringArray(R.array.notification_times)[selectIndex], Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param hour
     * @param minute
     * @param selectIndex
     */
    private void updateServiceData(int hour,int minute,int selectIndex) {
        Message message = WorkoutService.serviceHandler.obtainMessage(MSG_UPDATE_INTERVAL, hour, minute,selectIndex);
        message.sendToTarget();
    }
}