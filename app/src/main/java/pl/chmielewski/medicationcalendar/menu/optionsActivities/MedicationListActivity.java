package pl.chmielewski.medicationcalendar.menu.optionsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import pl.chmielewski.medicationcalendar.Medicament;
import pl.chmielewski.medicationcalendar.MedicationCalendarDatabaseHelper;
import pl.chmielewski.medicationcalendar.menu.optionsActivities.detailedActivities.MedicamentDetailsActivity;
import pl.chmielewski.medicationcalendar.R;

public class MedicationListActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

//        ArrayAdapter<Medicament> listAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                Medicament.medicaments);
//        ListView listMedications = findViewById(R.id.list_medications);
//        listMedications.setAdapter(listAdapter);
        ListView listMedicaments = (ListView) findViewById(R.id.list_medications);
        SQLiteOpenHelper medicationCalendarDatabaseHelper = new MedicationCalendarDatabaseHelper(this);
        try {

            db = medicationCalendarDatabaseHelper.getReadableDatabase();
            //Zapytanie pobierające elementy id i nazwy z tabeli Medicament
            cursor = db.query("MEDICAMENT",
                    new String[]{"_id", "NAME"},
                    null, null, null, null, null);

            //Tworzenie adaptera kursora
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);

            listMedicaments.setAdapter(listAdapter);


            }catch (SQLiteException e){
            Toast toast = Toast.makeText(this, "Baza danych jest niedostępna",
                    Toast.LENGTH_SHORT);
            toast.show();
        }


        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> listDrinks,
                                            View itemView,
                                            int position,
                                            long id) {
                        //Przekazujemy kliknięty napój do DrinkActivity.
                        Intent intent = new Intent(MedicationListActivity.this,
                                MedicamentDetailsActivity.class);
                        intent.putExtra(MedicamentDetailsActivity.EXTRA_MEDICAMENTID, (int) id);
                        startActivity(intent);
                    }
                };
        //Przypisujemy obiekt nasłuchujący do widoku listy.
        listMedicaments.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
