package pl.chmielewski.medicationcalendar.menu.optionsActivities.detailedActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pl.chmielewski.medicationcalendar.Medicament;
import pl.chmielewski.medicationcalendar.MedicationCalendarDatabaseHelper;
import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.menu.OptionsActivity;
import pl.chmielewski.medicationcalendar.menu.optionsActivities.AddMedicamentActivity;

public class MedicamentDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MEDICAMENTID = "medicamentId";
    String nameText;
    double doseOfMedicament;
    String doseUnit;
    int dailyDosingFrequency;
    String additionalMedicamentInfo;
    int medicamentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament_details);

        // Pobieramy identyfikator leku z intencji
         medicamentId = (Integer)getIntent().getExtras().get(EXTRA_MEDICAMENTID);
        SQLiteOpenHelper medicationCalendarDatabaseHelper = new MedicationCalendarDatabaseHelper(this);

        //Pobieranie całego rekordu o id z medicamentId przesyłanego z poprzedniej aktywności z bazy danych
        try{
            SQLiteDatabase db=medicationCalendarDatabaseHelper.getReadableDatabase();
            Cursor cursor = db.query("MEDICAMENT",new String[]{
                    "NAME",
                    "DOSE_OF_MEDICAMENT",
                    "DOSE_UNIT",
                    "DAILY_DOSING_FREQUENCY",
                    "ADDITIONAL_MEDICAMENT_INFO"},"_id = ?",new String[]{Integer.toString(medicamentId)},null,null,null);

            //Jeżeli został znaleziony rekord w bazie o id: medicamentId i zapisany w kursorze to pobierane są z niego wartości do widoków
            if(cursor.moveToFirst()){
                nameText = cursor.getString(0);
                doseOfMedicament = cursor.getDouble(1);
                doseUnit = cursor.getString(2);
                dailyDosingFrequency = cursor.getInt(3);
                additionalMedicamentInfo = cursor.getString(4);

                // Wyświetlamy nazwę leku
                TextView name = (TextView)findViewById(R.id.MedicamentDetailsTextViewName);
                name.setText(nameText);

                // Wyświetlamy dawkę leku
                TextView medicineDose = (TextView)findViewById(R.id.medicamentDetailsTextViewMedicamentDose);
                medicineDose.setText(doseOfMedicament +doseUnit);

                // Wyświetlamy ilość dawek leku przyjmowanych w ciągu dnia
                TextView textViewDailyDosingFrequency = (TextView) findViewById(R.id.medicamentDetailsTextViewDailyDosingFrequency);
                textViewDailyDosingFrequency.setText(Integer.toString(dailyDosingFrequency));

                // Wyświetlamy dodatkowe informacje o leku
                TextView textViewAdditionalMedicamentInfo = findViewById(R.id.medicationDetailsTextviewAdditionalMedicamentInfo);
                textViewAdditionalMedicamentInfo.setText(additionalMedicamentInfo);
            }
            //zamykamy kursor i baze danych
            cursor.close();
            db.close();

        //w przeciwnym wypadku wyrzucamy komunikat o niedostępności bazy danych
        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this,"Baza danych jest niedostępna",Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    public void deleteMedicamentFromDatabase(View view) {
        SQLiteOpenHelper medicationCalendarDatabaseHelper = new MedicationCalendarDatabaseHelper(this);
        TextView name = (TextView)findViewById(R.id.MedicamentDetailsTextViewName);
        SQLiteDatabase db=medicationCalendarDatabaseHelper.getWritableDatabase();
        try{

            db.delete("MEDICAMENT", "NAME = ?", new String[]{String.valueOf(name.getText())});

            Toast toast = Toast.makeText(this, "Element został usunięty pomyślnie.",
                    Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(MedicamentDetailsActivity.this,
                    OptionsActivity.class);
            startActivity(intent);

        }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
        db.close();
    }

    public void editMedicamentInDatabase(View view) {
        Intent addMedicamentIntent=new Intent(MedicamentDetailsActivity.this, AddMedicamentActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_NAMETEXT", nameText);
        extras.putString("EXTRA_DOSEOFMEDICAMENT", String.valueOf(doseOfMedicament));
        extras.putString("EXTRA_DOSEUNIT", doseUnit);
        extras.putString("EXTRA_DAILYDOSINGFREQUENCY", String.valueOf(dailyDosingFrequency));
        extras.putString("EXTRA_ADDITIONALMEDICAMENTINFO", additionalMedicamentInfo);

//        addMedicamentIntent.putExtra(AddMedicamentActivity.EXTRA_NAMETEXT, nameText);
//        addMedicamentIntent.putExtra(AddMedicamentActivity.EXTRA_DOSEOFMEDICAMENT,  doseOfMedicament);
//        addMedicamentIntent.putExtra(AddMedicamentActivity.EXTRA_DOSEUNIT, doseUnit);
//        addMedicamentIntent.putExtra(AddMedicamentActivity.EXTRA_DAILYDOSINGFREQUENCY, dailyDosingFrequency);
//        addMedicamentIntent.putExtra(AddMedicamentActivity.EXTRA_ADDITIONALMEDICAMENTINFO, additionalMedicamentInfo);
        addMedicamentIntent.putExtras(extras);
        addMedicamentIntent.putExtra(AddMedicamentActivity.EXTRA_MEDICAMENTID,medicamentId);
        startActivity(addMedicamentIntent);

    }

    public void setNotification(View view) {
        Intent setNotificationIntent=new Intent(MedicamentDetailsActivity.this,SetNotificationActivity.class);
        Bundle extrasToSetNotificationIntent = new Bundle();
        extrasToSetNotificationIntent.putString("EXTRA_NAMETEXT", nameText);
        extrasToSetNotificationIntent.putString("EXTRA_DOSEOFMEDICAMENT", String.valueOf(doseOfMedicament));
        extrasToSetNotificationIntent.putString("EXTRA_DOSEUNIT", doseUnit);
        extrasToSetNotificationIntent.putString("EXTRA_DAILYDOSINGFREQUENCY", String.valueOf(dailyDosingFrequency));
        extrasToSetNotificationIntent.putString("EXTRA_ADDITIONALMEDICAMENTINFO", additionalMedicamentInfo);
        startActivity(setNotificationIntent);
    }
}
