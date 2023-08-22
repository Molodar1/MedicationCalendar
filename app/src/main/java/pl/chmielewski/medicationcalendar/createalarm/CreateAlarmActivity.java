package pl.chmielewski.medicationcalendar.createalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Random;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.alarmslist.AlarmsListActivity;
import pl.chmielewski.medicationcalendar.data.Alarm;
import pl.chmielewski.medicationcalendar.databinding.ActivityCreateAlarmBinding;

public class CreateAlarmActivity extends AppCompatActivity {

    private ActivityCreateAlarmBinding binding;
    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);

        // Inicjalizacja widoków
        TimePicker timePicker = binding.fragmentCreatealarmTimePicker;
        EditText title = binding.fragmentCreatealarmTitle;
        Button scheduleAlarm = binding.fragmentCreatealarmScheduleAlarm;
        CheckBox recurring = binding.fragmentCreatealarmRecurring;
        CheckBox mon = binding.fragmentCreatealarmCheckMon;
        CheckBox tue = binding.fragmentCreatealarmCheckTue;
        CheckBox wed = binding.fragmentCreatealarmCheckWed;
        CheckBox thu = binding.fragmentCreatealarmCheckThu;
        CheckBox fri = binding.fragmentCreatealarmCheckFri;
        CheckBox sat = binding.fragmentCreatealarmCheckSat;
        CheckBox sun = binding.fragmentCreatealarmCheckSun;
        LinearLayout recurringOptions = binding.fragmentCreatealarmRecurringOptions;

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                navigateToAlarmsListActivity();
            }
        });
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                binding.fragmentCreatealarmTimePicker.getHour(),
                binding.fragmentCreatealarmTimePicker.getMinute(),
                binding.fragmentCreatealarmTitle.getText().toString(),
                System.currentTimeMillis(),
                true,
                binding.fragmentCreatealarmRecurring.isChecked(),
                binding.fragmentCreatealarmCheckMon.isChecked(),
                binding.fragmentCreatealarmCheckTue.isChecked(),
                binding.fragmentCreatealarmCheckWed.isChecked(),
                binding.fragmentCreatealarmCheckThu.isChecked(),
                binding.fragmentCreatealarmCheckFri.isChecked(),
                binding.fragmentCreatealarmCheckSat.isChecked(),
                binding.fragmentCreatealarmCheckSun.isChecked()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(this);
    }

    private void navigateToAlarmsListActivity() {
        Intent intent = new Intent(this, AlarmsListActivity.class);
        startActivity(intent);
        finish();
    }
}