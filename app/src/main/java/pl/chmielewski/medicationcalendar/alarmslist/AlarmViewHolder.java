package pl.chmielewski.medicationcalendar.alarmslist;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.data.Alarm;


public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private ImageView alarmRecurring;
    private TextView alarmRecurringDays;
    private TextView alarmTitle;

    Switch alarmStarted;
    ImageButton deleteAlarm;

    private OnToggleAlarmListener listener;
    private OnDeleteAlarmListener deleteListener;

    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener,OnDeleteAlarmListener deleteListener) {
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        deleteAlarm=itemView.findViewById(R.id.item_alarm_delete);
        alarmRecurring = itemView.findViewById(R.id.item_alarm_recurring);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);

        this.listener = listener;
        this.deleteListener=deleteListener;
    }

    public void bind(Alarm alarm) {
        String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());

        if (alarm.isRecurring()) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
            alarmRecurringDays.setText("Jednorazowy");
        }

        if (alarm.getMedicamentName().length() != 0) {
            alarmTitle.setText(String.format("%s\nDawka leku: %s", alarm.getMedicamentName(),alarm.getMedicamentDose()));
        } else {
            alarmTitle.setText(String.format("%s | %d", "Alarm", alarm.getAlarmId()));
        }

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onToggle(alarm);
            }
        });
        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onDelete(alarm);
            }
        });
    }
}
