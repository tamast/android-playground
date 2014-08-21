package com.taktam.android.quiethours;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.taktam.android.quiethours.models.Schedule;

import java.util.Calendar;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_quiet_hours)
public class QuietHoursActivity extends RoboActivity {

    private static final String TAG = QuietHoursActivity.class.getName();

    @InjectView(R.id.startTimePicker)
    private TimePicker startTimePicker;

    @InjectView(R.id.endTimePicker)
    private TimePicker endTimePicker;

    @InjectView(R.id.enabledCheckBox)
    private CheckBox enabledCheckBox;

    @InjectView(R.id.saveButton)
    private Button saveBtn;

    private Schedule schedule;

    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        schedule = Schedule.findById(Schedule.class, 1L);
        Log.i(TAG, "schedule: " + schedule.toString());

        alarmManager = (AlarmManager) QuietHoursActivity.this.getSystemService(ALARM_SERVICE);

        saveBtn.setOnClickListener(view -> {
            if (schedule == null) {
                schedule = new Schedule();
            }

            schedule.setStartHour(startTimePicker.getCurrentHour());
            schedule.setStartMinute(startTimePicker.getCurrentMinute());
            schedule.setEndHour(endTimePicker.getCurrentHour());
            schedule.setEndMinute(endTimePicker.getCurrentMinute());
            schedule.setEnabled(enabledCheckBox.isChecked());
            schedule.save();

            if (schedule.isEnabled()) {
                setAlarms(schedule);
            } else {
                cancelAlarms();
            }

            Toast.makeText(getApplicationContext(), "Successfully saved!", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (schedule != null) {
            startTimePicker.setCurrentHour(schedule.getStartHour());
            startTimePicker.setCurrentMinute(schedule.getStartMinute());
            endTimePicker.setCurrentHour(schedule.getEndHour());
            endTimePicker.setCurrentMinute(schedule.getEndMinute());
            enabledCheckBox.setChecked(schedule.isEnabled());
        }
    }

    private void setAlarms(final Schedule schedule) {
        setAlarm("com.taktam.android.quiethours.mute", schedule.getStartHour(), schedule.getStartMinute());
        setAlarm("com.taktam.android.quiethours.reinstate", schedule.getEndHour(), schedule.getEndMinute());
    }

    private void setAlarm(final String action, final int hour, final int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(QuietHoursActivity.this, 0, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelAlarms() {
        cancelAlarm("com.taktam.android.quiethours.mute");
        cancelAlarm("com.taktam.android.quiethours.reinstate");
    }

    private void cancelAlarm(final String action) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(QuietHoursActivity.this, 0, new Intent(action), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
