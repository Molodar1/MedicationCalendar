package pl.chmielewski.medicationcalendar.menu.optionsActivities.detailedActivities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pl.chmielewski.medicationcalendar.EventMessageFormater;
import pl.chmielewski.medicationcalendar.MedicationCalendarDatabaseHelper;
import pl.chmielewski.medicationcalendar.R;

public class SetNotificationActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_time, btn_date, btn_done;
    String timeTonotify;
    String nameText="";
    String doseOfMedicament="";
    String doseUnit="";
    String dailyDosingFrequency="";
    String additionalMedicamentInfo="";
    String eventMessage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_notification);
        btn_time = findViewById(R.id.btn_time);
        btn_date = findViewById(R.id.btn_date);
        btn_done = findViewById(R.id.btn_done);
        btn_time.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_done.setOnClickListener(this);

        try {
            Bundle extras = getIntent().getExtras();
            nameText=extras.getString("EXTRA_NAMETEXT");
            doseOfMedicament=extras.getString("EXTRA_DOSEOFMEDICAMENT");
            doseUnit=extras.getString("EXTRA_DOSEUNIT");
            dailyDosingFrequency=extras.getString("EXTRA_DAILYDOSINGFREQUENCY");
            additionalMedicamentInfo=extras.getString("EXTRA_ADDITIONALMEDICAMENTINFO");
            eventMessage=EventMessageFormater.formatEventMessage(doseOfMedicament,doseUnit,additionalMedicamentInfo);
        }catch (NullPointerException n){}
    }

    @Override
    public void onClick(View view) {
         if (view == btn_time) {
            selectTime();
        } else if (view == btn_date) {
            selectDate();
        } else {
           // submit();
        }
    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                btn_time.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btn_date.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

//    private void submit() {
//            if (btn_time.getText().toString().equals(getString(R.string.setNotificationBtnTimeText)) || btn_date.getText().toString().equals(getString(R.string.setNotificationBtnDateText))) {
//                Toast.makeText(this, "Proszę wybrać datę i godzinę.", Toast.LENGTH_SHORT).show();
//            } else {
//                String name =nameText;
//                String time = (btn_time.getText().toString().trim());
//                String date = (btn_date.getText().toString().trim());
//                MedicationCalendarDatabaseHelper medicationCalendarDatabaseHelper = new MedicationCalendarDatabaseHelper(this);
//                SQLiteDatabase db = medicationCalendarDatabaseHelper.getWritableDatabase();
//                MedicationCalendarDatabaseHelper.insertEvent(db,name,time,date,eventMessage);
//                db.close();
//                setAlarm(name, time, date, eventText);
//            }
//        }
//    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }
}
