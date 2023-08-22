//package pl.chmielewski.medicationcalendar.createalarm;
//
//import android.os.Bundle;
//
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TimePicker;
//
//import pl.chmielewski.medicationcalendar.data.Alarm;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.navigation.Navigation;
//
//import pl.chmielewski.medicationcalendar.R;
//
//import java.util.Objects;
//import java.util.Random;
//
//
//import pl.chmielewski.medicationcalendar.databinding.FragmentCreatealarmBinding;
//
//public class CreateAlarmFragment extends Fragment {
//    private FragmentCreatealarmBinding binding;
//    private CreateAlarmViewModel createAlarmViewModel;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentCreatealarmBinding.inflate(inflater, container, false);
//        View view = binding.getRoot();
//
//        // Inicjalizacja widoków
//        TimePicker timePicker = binding.fragmentCreatealarmTimePicker;
//        EditText title = binding.fragmentCreatealarmTitle;
//        Button scheduleAlarm = binding.fragmentCreatealarmScheduleAlarm;
//        CheckBox recurring = binding.fragmentCreatealarmRecurring;
//        CheckBox mon = binding.fragmentCreatealarmCheckMon;
//        CheckBox tue = binding.fragmentCreatealarmCheckTue;
//        CheckBox wed = binding.fragmentCreatealarmCheckWed;
//        CheckBox thu = binding.fragmentCreatealarmCheckThu;
//        CheckBox fri = binding.fragmentCreatealarmCheckFri;
//        CheckBox sat = binding.fragmentCreatealarmCheckSat;
//        CheckBox sun = binding.fragmentCreatealarmCheckSun;
//        LinearLayout recurringOptions = binding.fragmentCreatealarmRecurringOptions;
//
//        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    recurringOptions.setVisibility(View.VISIBLE);
//                } else {
//                    recurringOptions.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scheduleAlarm();
//                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
//            }
//        });
//
//        return view;
//    }
//
//    private void scheduleAlarm() {
//        int alarmId = new Random().nextInt(Integer.MAX_VALUE);
//
//        Alarm alarm = new Alarm(
//                alarmId,
//                binding.fragmentCreatealarmTimePicker.getHour(),
//                binding.fragmentCreatealarmTimePicker.getMinute(),
//                binding.fragmentCreatealarmTitle.getText().toString(),
//                System.currentTimeMillis(),
//                true,
//                binding.fragmentCreatealarmRecurring.isChecked(),
//                binding.fragmentCreatealarmCheckMon.isChecked(),
//                binding.fragmentCreatealarmCheckTue.isChecked(),
//                binding.fragmentCreatealarmCheckWed.isChecked(),
//                binding.fragmentCreatealarmCheckThu.isChecked(),
//                binding.fragmentCreatealarmCheckFri.isChecked(),
//                binding.fragmentCreatealarmCheckSat.isChecked(),
//                binding.fragmentCreatealarmCheckSun.isChecked()
//        );
//
//        createAlarmViewModel.insert(alarm);
//
//        alarm.schedule(requireContext());
//    }
//}
