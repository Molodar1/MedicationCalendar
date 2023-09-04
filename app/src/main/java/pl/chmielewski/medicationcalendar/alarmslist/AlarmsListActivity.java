package pl.chmielewski.medicationcalendar.alarmslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import pl.chmielewski.medicationcalendar.createalarm.CreateAlarmActivity;
import pl.chmielewski.medicationcalendar.data.alarm.Alarm;
import pl.chmielewski.medicationcalendar.databinding.ActivityAlarmsListBinding;

public class AlarmsListActivity extends AppCompatActivity implements OnToggleAlarmListener,OnDeleteAlarmListener{

    private ActivityAlarmsListBinding binding;
    private AlarmsListViewModel alarmsListViewModel;
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        alarmsListViewModel = ViewModelProviders.of(this).get(AlarmsListViewModel.class);
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this,this);

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

    @Override
    public void onDelete(Alarm alarm) {
       if (alarm.isStarted()){
           alarm.cancelAlarm(getApplicationContext());
           alarmsListViewModel.delete(alarm);
       }else {
           alarmsListViewModel.delete(alarm);
       }

    }
}