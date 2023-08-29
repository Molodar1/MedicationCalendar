package pl.chmielewski.medicationcalendar.alarmslist;


import pl.chmielewski.medicationcalendar.data.alarm.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
}
