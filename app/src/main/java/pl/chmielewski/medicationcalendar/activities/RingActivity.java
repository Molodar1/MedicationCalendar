package pl.chmielewski.medicationcalendar.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;


import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.data.alarm.Alarm;
import pl.chmielewski.medicationcalendar.data.medicament.MedicamentRepository;
import pl.chmielewski.medicationcalendar.databinding.ActivityRingBinding;
import pl.chmielewski.medicationcalendar.service.AlarmService;

public class RingActivity extends AppCompatActivity {

    ActivityRingBinding binding;
    DatabaseReference medicamentNumberOfDosesRef;
    private String medicamentName,medicamentDose,medicamentAdditionalInfo,userId,medicamentKey,medicamentNumberOfDoses;
    private int medicamentNumberOfDosesFromDB;
    private MedicamentRepository medicamentRepository;
    private Alarm alarm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        medicamentRepository = new MedicamentRepository(getApplication());
        Medicament medicament=(Medicament) getIntent().getSerializableExtra("MEDICAMENT_OBJECT");
        alarm=(Alarm) getIntent().getSerializableExtra("ALARM_OBJECT");
            getMedicineNumberOfDosesFromDB(medicament);


        String alarmText = getIntent().getStringExtra("alarmText");
        if (alarmText!=null){
            String[] alarmTextParts = alarmText.split("\n");
            if (alarmTextParts[0]!=null){
                medicamentName=medicament.getMedicamentName();
                binding.textViewMedicamentName.setText(medicamentName);
            }else medicamentName="";
            if (alarmTextParts[1]!=null){
                medicamentDose=alarmTextParts[1];
                binding.textViewmedicamentDose.setText(medicamentDose);
            }else medicamentDose="";
            if (alarmTextParts[2]!=null){
                medicamentAdditionalInfo=alarmTextParts[2].replace("Dodatkowe informacje: ", "");
                binding.textViewMedicamentAdditionalInfo.setText(medicamentAdditionalInfo);
            }else medicamentAdditionalInfo="";
        }

        binding.activityRingDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });

        binding.activityRingSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 10);

                Alarm alarm = new Alarm(
                        new Random().nextInt(Integer.MAX_VALUE),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        System.currentTimeMillis(),
                        medicament,
                        true
                );
                alarm.schedule(getApplicationContext());

                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });

        animateClock();
    }

    private void getMedicineNumberOfDosesFromDB(Medicament passedMedicament) {
        Medicament medicamentFromDB = medicamentRepository.getMedicamentByIdSync(passedMedicament.getMedicamentId());
        if (medicamentFromDB != null) {
            int medicamentNumberOfDosesFromDB = medicamentFromDB.getMedicamentNumberOfDoses();
            if (!alarm.isSnoozed()){
                medicamentNumberOfDosesFromDB--;
            }
            if (medicamentNumberOfDosesFromDB<0){
                medicamentNumberOfDosesFromDB=0;
            }

            medicamentFromDB.setMedicamentNumberOfDoses(medicamentNumberOfDosesFromDB);
            medicamentRepository.update(medicamentFromDB);
            if (medicamentNumberOfDosesFromDB<=5){
                if (medicamentNumberOfDosesFromDB==0){
                    binding.textViewmedicamentNumberOfDoses.setText("BRAK KOLEJNYCH DAWEK LEKU!");
                    binding.textViewmedicamentNumberOfDoses.setTextColor(Color.RED);
                }
                else {
                    medicamentNumberOfDoses = String.valueOf(medicamentNumberOfDosesFromDB);
                    binding.textViewmedicamentNumberOfDoses.setText("LICZBA POZOSTAŁYCH DAWEK: "+medicamentNumberOfDoses);
                    binding.textViewmedicamentNumberOfDoses.setTextColor(Color.RED);
                }
            } else {
                medicamentNumberOfDoses = String.valueOf(medicamentNumberOfDosesFromDB);
                binding.textViewmedicamentNumberOfDoses.setText("Pozostało: "+medicamentNumberOfDoses+" dawek leku.");
            }


        }
    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(binding.activityRingClock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }
}