package com.example.workouttrack;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkoutManagerTest {

    @Mock
    Context mockContext;

    @Mock
    AlarmManager mockAlarmManager;

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    PendingIntent mockPendingIntent;

    @Before
    public void setUp() {
        //MockitoAnnotations.initMocks(this);
        when(mockContext.getSharedPreferences(any(String.class), any(Integer.class))).thenReturn(mockSharedPreferences);
        when(mockContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mockAlarmManager);
        when(mockPendingIntent.getBroadcast(any(Context.class), any(Integer.class), any(Intent.class), any(Integer.class))).thenReturn(mockPendingIntent);
    }

    @Test
    public void setTrigger_shouldSetAlarm() {
        WorkoutManager workoutManager = WorkoutManager.getInstance();
        int hour = 12;
        int minute = 30;
        int addDay = 1;
        int selectIndex = 2;

        workoutManager.setTrigger(mockContext, hour, minute, addDay, selectIndex);
        verify(mockAlarmManager).setExact(any(Integer.class), any(Long.class), any(PendingIntent.class));
    }

    @Test
    public void setNextTrigger_shouldSetNextAlarm() {
        WorkoutManager workoutManager = WorkoutManager.getInstance();
        when(mockSharedPreferences.getInt("hour", WorkoutManager.DEFAULT_TRIGGER_HOUR)).thenReturn(12);
        when(mockSharedPreferences.getInt("minute", WorkoutManager.DEFAULT_TRIGGER_MINUTE)).thenReturn(30);
        when(mockSharedPreferences.getInt("notificationIndex", 1)).thenReturn(2);

        workoutManager.setNextTrigger(mockContext);
        verify(mockAlarmManager).setExact(any(Integer.class), any(Long.class), any(PendingIntent.class));
    }
}
