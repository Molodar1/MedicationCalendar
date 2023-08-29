package pl.chmielewski.medicationcalendar.broadcastreceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

import pl.chmielewski.medicationcalendar.data.alarm.Alarm;
import pl.chmielewski.medicationcalendar.service.AlarmService;
import pl.chmielewski.medicationcalendar.service.RescheduleAlarmsService;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String MEDICAMENT_NAME = "MEDICAMENT_NAME";
    public static final String MEDICAMENT_KEY = "MEDICAMENT_KEY";
    public static final String USER_ID = "USER_ID";
    public static final String MEDICAMENT_DOSE = "MEDICAMENT_DOSE";
    public static final String MEDICAMENT_NUMBER_OF_DOSES = "MEDICAMENT_NUMBER_OF_DOSES";
    public static final String MEDICAMENT_ADDITIONAL_INFO = "MEDICAMENT_ADDITIONAL_INFO";
    public static final String ALARM_ID = "ALARM_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        }
        else {
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (!intent.getBooleanExtra(RECURRING, false)) {
                startAlarmService(context, intent);
            }else {

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.DAY_OF_MONTH, 1);

                 int alarmId=intent.getIntExtra(ALARM_ID,0);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_NO_CREATE);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        // Schedule the exact alarm
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                    }
                } else {
                    // Schedule the exact alarm for older versions of Android
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                if (alarmIsToday(intent)) {
                    intent.removeExtra("ALARM_OBJECT");

                    startAlarmService(context, intent);
                }
            }
        }
    }

    private boolean alarmIsToday(Intent intent) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (intent.getBooleanExtra(MONDAY, false))
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (intent.getBooleanExtra(TUESDAY, false))
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (intent.getBooleanExtra(WEDNESDAY, false))
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (intent.getBooleanExtra(THURSDAY, false))
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (intent.getBooleanExtra(FRIDAY, false))
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (intent.getBooleanExtra(SATURDAY, false))
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (intent.getBooleanExtra(SUNDAY, false))
                    return true;
                return false;
        }
        return false;
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        Alarm alarm = (Alarm) intent.getSerializableExtra("ALARM_OBJECT");
        intentService.putExtra(MEDICAMENT_NAME, intent.getStringExtra(MEDICAMENT_NAME));
        intentService.putExtra(MEDICAMENT_KEY, intent.getStringExtra(MEDICAMENT_KEY));
        intentService.putExtra(MEDICAMENT_DOSE, intent.getStringExtra(MEDICAMENT_DOSE));
        intentService.putExtra(MEDICAMENT_NUMBER_OF_DOSES, intent.getStringExtra(MEDICAMENT_NUMBER_OF_DOSES));
        intentService.putExtra(MEDICAMENT_ADDITIONAL_INFO, intent.getStringExtra(MEDICAMENT_ADDITIONAL_INFO));
        intentService.putExtra(ALARM_ID, intent.getIntExtra(ALARM_ID,0));
        intentService.putExtra(USER_ID, intent.getStringExtra(USER_ID));
        intentService.putExtra("ALARM_OBJECT",alarm);
        context.startForegroundService(intentService);
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        context.startForegroundService(intentService);
    }
}
