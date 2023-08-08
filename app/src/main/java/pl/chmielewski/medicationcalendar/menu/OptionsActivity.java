package pl.chmielewski.medicationcalendar.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.menu.optionsActivities.AddMedicamentActivity;
import pl.chmielewski.medicationcalendar.menu.optionsActivities.MedicationListActivity;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

// Creating Listener for MedicationList
        AdapterView.OnItemClickListener itemClickListener = (listView, v, position, id) -> {
            if (position == 1) {
                Intent intent = new Intent(OptionsActivity.this,
                        MedicationListActivity.class);
                startActivity(intent);
            } else if (position == 0) {
                Intent intent = new Intent(OptionsActivity.this,
                        AddMedicamentActivity.class);
                startActivity(intent);
            }
        };
        ListView listView = findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }
}
