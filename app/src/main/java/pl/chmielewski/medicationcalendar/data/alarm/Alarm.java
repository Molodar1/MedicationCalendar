package pl.chmielewski.medicationcalendar.data.alarm;



import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.ALARM_ID;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.FRIDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_ADDITIONAL_INFO;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_DOSE;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_KEY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_NUMBER_OF_DOSES;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MONDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.RECURRING;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.SATURDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.SUNDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.THURSDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.MEDICAMENT_NAME;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.TUESDAY;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.USER_ID;
import static pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver.WEDNESDAY;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Calendar;

import pl.chmielewski.medicationcalendar.broadcastreceiver.AlarmBroadcastReceiver;

@Entity(tableName = "alarm_table")
public class Alarm implements Serializable {

    @PrimaryKey
    @NonNull
    private int alarmId;
    private String userId;
    private int hour, minute;
    private boolean started, recurring;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private String medicamentName,medicamentKey;
    private long created;
    private String medicamentDose;
    private String medicamentNumberOfDoses;
    private String medicamentAdditionalInfo;



    public Alarm(int alarmId, String userId, int hour, int minute,String medicamentKey, String medicamentName, String medicamentDose, String medicamentNumberOfDoses, String medicamentAdditionalInfo, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        this.alarmId = alarmId;
        this.userId=userId;
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
        this.medicamentKey=medicamentKey;
        this.medicamentName = medicamentName;
        this.medicamentDose=medicamentDose;
        this.medicamentNumberOfDoses=medicamentNumberOfDoses;
        this.medicamentAdditionalInfo=medicamentAdditionalInfo;

        this.created = created;
    }
    @Ignore
    public Alarm(int alarmId, int hour, int minute, String medicamentName, String medicamentDose, String medicamentNumberOfDoses, String medicamentAdditionalInfo, long created, boolean started, boolean recurring, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
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
        this.medicamentName = medicamentName;
        this.medicamentDose=medicamentDose;
        this.medicamentNumberOfDoses=medicamentNumberOfDoses;
        this.medicamentAdditionalInfo=medicamentAdditionalInfo;

        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMedicamentKey() {
        return medicamentKey;
    }

    public void setMedicamentKey(String medicamentKey) {
        this.medicamentKey = medicamentKey;
    }

    public String getMedicamentNumberOfDoses() {
        return medicamentNumberOfDoses;
    }

    public void setMedicamentNumberOfDoses(String medicamentNumberOfDoses) {
        this.medicamentNumberOfDoses = medicamentNumberOfDoses;
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

    public String getMedicamentDose() {
        return medicamentDose;
    }

    public void setMedicamentDose(String medicamentDose) {
        this.medicamentDose = medicamentDose;
    }

    public String getMedicamentAdditionalInfo() {
        return medicamentAdditionalInfo;
    }

    public void setMedicamentAdditionalInfo(String medicamentAdditionalInfo) {
        this.medicamentAdditionalInfo = medicamentAdditionalInfo;
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
        intent.putExtra(USER_ID,userId);
        intent.putExtra(MEDICAMENT_KEY,medicamentKey);
        intent.putExtra(MEDICAMENT_NAME, medicamentName);
        intent.putExtra(MEDICAMENT_DOSE, medicamentDose);
        intent.putExtra(MEDICAMENT_NUMBER_OF_DOSES, medicamentNumberOfDoses);
        intent.putExtra(MEDICAMENT_ADDITIONAL_INFO, medicamentAdditionalInfo);
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
                toastText = String.format("Jednorazowy Alarm %s został ustawiony", medicamentName);
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
                String toastText = String.format("Cykliczny Alarm %s ustawiony na dni: %s o godz. %02d:%02d", medicamentName, getRecurringDaysText(), hour, minute, alarmId);
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
        alarmManager.cancel(alarmPendingIntent);
        this.started = false;

        String toastText = String.format("Alarm %s na godzine %02d:%02d został anulowany",medicamentName, hour, minute);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
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

    public String getMedicamentName() {
        return medicamentName;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
