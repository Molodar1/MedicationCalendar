package pl.chmielewski.medicationcalendar.alarmsList;

import pl.chmielewski.medicationcalendar.data.alarm.Alarm;

public interface OnDeleteAlarmListener {

    void onDelete(Alarm alarm);
}
