package pl.chmielewski.medicationcalendar.alarmsList;


import pl.chmielewski.medicationcalendar.data.alarm.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
}
