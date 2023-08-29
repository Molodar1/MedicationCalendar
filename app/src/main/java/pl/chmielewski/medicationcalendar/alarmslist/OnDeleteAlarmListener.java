package pl.chmielewski.medicationcalendar.alarmslist;

import pl.chmielewski.medicationcalendar.data.alarm.Alarm;

public interface OnDeleteAlarmListener {

    void onDelete(Alarm alarm);
}
