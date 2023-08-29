package pl.chmielewski.medicationcalendar.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
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
import pl.chmielewski.medicationcalendar.databinding.ActivityRingBinding;
import pl.chmielewski.medicationcalendar.service.AlarmService;

public class RingActivity extends AppCompatActivity {

    ActivityRingBinding binding;
    DatabaseReference medicamentNumberOfDosesRef;
    private String medicamentName,medicamentDose,medicamentAdditionalInfo,userId,medicamentKey,medicamentNumberOfDoses;
    private int medicamentNumberOfDosesFromDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        System.out.println(medicamentKey);
        userId=getIntent().getStringExtra("USER_ID");
        medicamentKey=getIntent().getStringExtra("MEDICAMENT_KEY");
        //medicamentKey.replace("-","");
        if(userId!=null){
            getMedicineNumberOfDosesFromDB();
        }
        else {
            medicamentNumberOfDoses=getIntent().getStringExtra("MEDICAMENT_NUMBER_OF_DOSES");
            binding.textViewmedicamentNumberOfDoses.setText(String.valueOf(medicamentNumberOfDoses));
        }

        String alarmText = getIntent().getStringExtra("alarmText");
        if (alarmText!=null){
            String[] alarmTextParts = alarmText.split("\n");
            if (alarmTextParts[0]!=null){
                medicamentName=alarmTextParts[0].replace("Nazwa leku: ", "");
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
                        medicamentName,
                        medicamentDose,
                        medicamentNumberOfDoses,
                        medicamentAdditionalInfo,
                        System.currentTimeMillis(),
                        true,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false
                );
                alarm.schedule(getApplicationContext());

                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });

        animateClock();
    }

    private void getMedicineNumberOfDosesFromDB() {
        medicamentNumberOfDosesRef= FirebaseDatabase.getInstance().getReference("Medicament")
                .child(userId)
                .child(medicamentKey);
        medicamentNumberOfDosesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Tutaj możesz pobrać wartość "medicamentNumberOfDoses" z dataSnapshot
                if (dataSnapshot.exists()) {
                    Medicament medicament=dataSnapshot.getValue(Medicament.class);
                   medicamentNumberOfDosesFromDB = medicament.getMedicamentNumberOfDoses();
                   medicamentNumberOfDosesFromDB--;
                    medicamentNumberOfDoses=String.valueOf(medicamentNumberOfDosesFromDB);
                    binding.textViewmedicamentNumberOfDoses.setText(medicamentNumberOfDoses);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Obsługa błędu
            }
        });
    }

    private void animateClock() {
        ObjectAnimator rotateAnimation = ObjectAnimator.ofFloat(binding.activityRingClock, "rotation", 0f, 20f, 0f, -20f, 0f);
        rotateAnimation.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnimation.setDuration(800);
        rotateAnimation.start();
    }
}