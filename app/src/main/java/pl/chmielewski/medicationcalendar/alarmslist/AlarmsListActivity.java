package pl.chmielewski.medicationcalendar.alarmslist;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.createalarm.CreateAlarmActivity;
import pl.chmielewski.medicationcalendar.data.Alarm;
import pl.chmielewski.medicationcalendar.databinding.ActivityAlarmsListBinding;

public class AlarmsListActivity extends AppCompatActivity implements OnToggleAlarmListener{

    private ActivityAlarmsListBinding binding;
    private AlarmsListViewModel alarmsListViewModel;
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);

        binding.fragmentListalarmsRecylerView.setLayoutManager(new LinearLayoutManager(this));
        binding.fragmentListalarmsRecylerView.setAdapter(alarmRecyclerViewAdapter);

        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms != null) {
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                }
            }
        });

        binding.fragmentListalarmsAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCreateAlarmActivity();
            }
        });
    }

    private void navigateToCreateAlarmActivity() {
        Intent intent = new Intent(this, CreateAlarmActivity.class);
        startActivity(intent);
    }

    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getApplicationContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(getApplicationContext());
            alarmsListViewModel.update(alarm);
        }
    }
}