package pl.chmielewski.medicationcalendar.menu.optionsActivities;

import androidx.appcompat.app.AppCompatActivity;

import pl.chmielewski.medicationcalendar.Medicament;
import pl.chmielewski.medicationcalendar.MedicationCalendarDatabaseHelper;
import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.menu.OptionsActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddMedicamentActivity extends AppCompatActivity {
    public static String EXTRA_MEDICAMENTID="";
    public static String EXTRA_NAMETEXT ="";
    public static String EXTRA_DOSEOFMEDICAMENT ="";
    public static String EXTRA_DOSEUNIT ="";
    public static String EXTRA_DAILYDOSINGFREQUENCY ="";
    public static String EXTRA_ADDITIONALMEDICAMENTINFO ="";



    EditText editTextMedicamentName;
    EditText editTextMedicamentDose;
    Spinner spinnerDoseUnit;
    Spinner spinnerDailyDosingFrequency;
    EditText editTextAdditionalMedicamentInfo;
    MedicationCalendarDatabaseHelper medicationCalendarDatabaseHelper;
    SQLiteDatabase db;

    int medicamentId;
    String nameText="";
    String doseOfMedicament="";
    String doseUnit="";
    String dailyDosingFrequency="";
    String additionalMedicamentInfo="";
    boolean isItEdited=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicament);
         editTextMedicamentName=findViewById(R.id.editTextInsertMedicamentName);
         editTextMedicamentDose=findViewById(R.id.editTextInsertMedicamentDose);
         spinnerDoseUnit=findViewById(R.id.spinnerDose);
         spinnerDailyDosingFrequency=findViewById(R.id.spinnerDailyDosingFrequency);
         editTextAdditionalMedicamentInfo=findViewById(R.id.editTextInsertAdditionalInfoAboutMedicament);
        MedicationCalendarDatabaseHelper medicationCalendarDatabaseHelper=new MedicationCalendarDatabaseHelper(this);
        SQLiteDatabase db = medicationCalendarDatabaseHelper.getWritableDatabase();

        try {
            Bundle extras = getIntent().getExtras();
            nameText=extras.getString("EXTRA_NAMETEXT");
            doseOfMedicament=extras.getString("EXTRA_DOSEOFMEDICAMENT");
            doseUnit=extras.getString("EXTRA_DOSEUNIT");
            dailyDosingFrequency=extras.getString("EXTRA_DAILYDOSINGFREQUENCY");
            additionalMedicamentInfo=extras.getString("EXTRA_ADDITIONALMEDICAMENTINFO");
            medicamentId =(Integer)getIntent().getExtras().get(EXTRA_MEDICAMENTID);
            isItEdited=true;
        }catch (NullPointerException n){}




         editTextMedicamentName.setText(nameText);
         editTextMedicamentDose.setText(doseOfMedicament);
         spinnerDoseUnit.setSelection(((ArrayAdapter)spinnerDoseUnit.getAdapter()).getPosition(doseUnit));
         spinnerDailyDosingFrequency.setSelection(((ArrayAdapter)spinnerDailyDosingFrequency.getAdapter()).getPosition(dailyDosingFrequency));
         editTextAdditionalMedicamentInfo.setText(additionalMedicamentInfo);

    }


    public void saveMedicamentToDatabase(View view) {
         medicationCalendarDatabaseHelper=new MedicationCalendarDatabaseHelper(this);
         db = medicationCalendarDatabaseHelper.getWritableDatabase();
            if (!isItEdited) {
                try {
                    MedicationCalendarDatabaseHelper.insertMedicament(
                            db,
                            String.valueOf(editTextMedicamentName.getText()),
                            Double.parseDouble(String.valueOf(editTextMedicamentDose.getText())),
                            String.valueOf(spinnerDoseUnit.getSelectedItem()),
                            Integer.parseInt(String.valueOf(spinnerDailyDosingFrequency.getSelectedItem())),
                            String.valueOf(editTextAdditionalMedicamentInfo.getText()));


                    Toast toast = Toast.makeText(this, "Lek został poprawnie wprowadzony do bazy danych", Toast.LENGTH_SHORT);
                    toast.show();
                    db.close();
                    Intent intent=new Intent(AddMedicamentActivity.this, OptionsActivity.class);
                    startActivity(intent);
                } catch (SQLiteException e) {
                    Toast toast = Toast.makeText(this, "Lek nie został wprowadzony spróbuj ponownie",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    db.close();

                }
            }
                else{
                    try {
                        MedicationCalendarDatabaseHelper.editMedicament(
                                db,
                                String.valueOf(editTextMedicamentName.getText()),
                                Double.parseDouble(String.valueOf(editTextMedicamentDose.getText())),
                                String.valueOf(spinnerDoseUnit.getSelectedItem()),
                                Integer.parseInt(String.valueOf(spinnerDailyDosingFrequency.getSelectedItem())),
                                String.valueOf(editTextAdditionalMedicamentInfo.getText()),
                                medicamentId);
                        Toast toast = Toast.makeText(this, "Lek został poprawnie edytowany w bazie danych", Toast.LENGTH_SHORT);
                        toast.show();
                        db.close();
                        isItEdited=false;
                        Intent intent=new Intent(AddMedicamentActivity.this, MedicationListActivity.class);
                        startActivity(intent);
                    }catch (SQLiteException e) {
                        Toast toast = Toast.makeText(this, "Lek nie został edytowany spróbuj ponownie",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        db.close();
                        isItEdited=false;
                    }
                }




    }

}
