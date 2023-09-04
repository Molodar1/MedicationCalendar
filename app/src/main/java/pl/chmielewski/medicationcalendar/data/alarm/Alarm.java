package pl.chmielewski.medicationcalendar.data.alarm;



import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.ALARM_ID;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.FRIDAY;

import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MONDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.RECURRING;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.SATURDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.SUNDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.THURSDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.TUESDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.WEDNESDAY;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

import pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;

@Entity(tableName = "alarm_table")
public class Alarm implements Serializable {

    @PrimaryKey
    @NonNull
    private int alarmId;
    private int hour, minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private long created;
    @Embedded
    private Medicament medicament;
    private boolean snoozed;

    public Alarm(int alarmId, int hour, int minute, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, long created, Medicament medicament) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.created = created;
        this.medicament = medicament;
    }

    @Ignore
    public Alarm(int alarmId, int hour, int minute, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, long created, Medicament medicament, boolean snoozed) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.created = created;
        this.medicament = medicament;
        this.snoozed = snoozed;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isStarted() {
        return started;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public boolean isSnoozed() {
        return snoozed;
    }

    public void setSnoozed(boolean snoozed) {
        this.snoozed = snoozed;
    }

    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, monday);
        intent.putExtra(TUESDAY, tuesday);
        intent.putExtra(WEDNESDAY, wednesday);
        intent.putExtra(THURSDAY, thursday);
        intent.putExtra(FRIDAY, friday);
        intent.putExtra(SATURDAY, saturday);
        intent.putExtra(SUNDAY, sunday);
        intent.putExtra("ALARM_OBJECT", this);
        intent.putExtra("MEDICAMENT_OBJECT",medicament);
        intent.putExtra(ALARM_ID,alarmId);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("Jednorazowy Alarm %s został ustawiony", medicament.getMedicamentName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            alarmPendingIntent
                    );
                }
                else {

                }
            }
            else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent
                );
            }
        }
        else {
                String toastText = String.format("Cykliczny Alarm %s ustawiony na dni: %s o godz. %02d:%02d", medicament.getMedicamentName(), getRecurringDaysText(), hour, minute, alarmId);
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

                final long RUN_DAILY = 24 * 60 * 60 * 1000;
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent
                );
            }


            this.started = true;
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (alarmPendingIntent != null) {
            alarmManager.cancel(alarmPendingIntent);
            this.started = false;

            String toastText = String.format("Alarm %s na godzine %02d:%02d został anulowany", medicament.getMedicamentName(), hour, minute);
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            Log.i("cancel", toastText);
        }
        else Log.i("cancel", "AlarmPendingIntent is null, alarm may not have been scheduled.");
    }

    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += "Pon ";
        }
        if (tuesday) {
            days += "Wt ";
        }
        if (wednesday) {
            days += "Śr ";
        }
        if (thursday) {
            days += "Czw ";
        }
        if (friday) {
            days += "Pt ";
        }
        if (saturday) {
            days += "Sob ";
        }
        if (sunday) {
            days += "Nd ";
        }

        return days;
    }
    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
