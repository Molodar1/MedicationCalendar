package pl.chmielewski.medicationcalendar.createAlarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.Random;

import pl.chmielewski.medicationcalendar.medicamentsList.RecyclerViewActivity;
import pl.chmielewski.medicationcalendar.data.alarm.Alarm;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.databinding.ActivityCreateAlarmBinding;

public class CreateAlarmActivity extends AppCompatActivity {

    private ActivityCreateAlarmBinding binding;
    private CreateAlarmViewModel createAlarmViewModel;
    private Medicament medicamentFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);

        // Inicjalizacja widoków
        TimePicker timePicker = binding.fragmentCreatealarmTimePicker;
        binding.fragmentCreatealarmTimePicker.setIs24HourView(true);
        TextView medicamentName = binding.fragmentCreatealarmTitle;
        TextView medicamentDose = binding.fragmentCreatealarmDose;
        TextView medicamentNumberOfDoses = binding.fragmentCreatealarmNumberOfDoses;
        TextView medicamentAdditionalInfo = binding.fragmentCreatealarmAdditionalInfo;
        Button scheduleAlarm = binding.fragmentCreatealarmScheduleAlarm;
        CheckBox recurring = binding.fragmentCreatealarmRecurring;
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
        Intent intent = getIntent();
        medicamentFromIntent = (Medicament) intent.getSerializableExtra("medicament");

        if (medicamentFromIntent != null) {
            String medicamentFromIntentName, medicamentFromIntentDose, medicamentFromIntentAdditionalInfo;
            int medicamentFromIntentNumberOfDoses;
            medicamentFromIntentName = medicamentFromIntent.getMedicamentName();
            medicamentFromIntentDose = medicamentFromIntent.getMedicamentDose();
            medicamentFromIntentAdditionalInfo = medicamentFromIntent.getMedicamentAdditionalInfo();
            medicamentFromIntentNumberOfDoses = medicamentFromIntent.getMedicamentNumberOfDoses();

            if (medicamentFromIntentName != null) {
                medicamentName.setText("Nazwa leku: " + medicamentFromIntent.getMedicamentName());
            }
            if (medicamentFromIntentDose != null) {
                medicamentDose.setText("Dawka leku: " + medicamentFromIntent.getMedicamentDose());
            }
            if (medicamentFromIntentAdditionalInfo != null) {
                medicamentAdditionalInfo.setText("Dodatkowe informacje: " + medicamentFromIntent.getMedicamentAdditionalInfo());
            }
            medicamentNumberOfDoses.setText("Pozostała liczba dawek: " + String.valueOf(medicamentFromIntentNumberOfDoses));

        }
    }

    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                binding.fragmentCreatealarmTimePicker.getHour(),
                binding.fragmentCreatealarmTimePicker.getMinute(),
                true,
                binding.fragmentCreatealarmRecurring.isChecked(),
                binding.fragmentCreatealarmCheckMon.isChecked(),
                binding.fragmentCreatealarmCheckTue.isChecked(),
                binding.fragmentCreatealarmCheckWed.isChecked(),
                binding.fragmentCreatealarmCheckThu.isChecked(),
                binding.fragmentCreatealarmCheckFri.isChecked(),
                binding.fragmentCreatealarmCheckSat.isChecked(),
                binding.fragmentCreatealarmCheckSun.isChecked(),
                System.currentTimeMillis(),
                medicamentFromIntent
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(this);
    }

    private void navigateToAlarmsListActivity() {
        Intent intent = new Intent(this, RecyclerViewActivity.class);
        startActivity(intent);
        finish();
    }
}