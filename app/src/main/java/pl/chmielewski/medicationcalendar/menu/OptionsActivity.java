package pl.chmielewski.medicationcalendar.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pl.chmielewski.medicationcalendar.menu.optionsActivities.AddMedicamentActivity;
import pl.chmielewski.medicationcalendar.menu.optionsActivities.DeleteMedicamentActivity;
import pl.chmielewski.medicationcalendar.menu.optionsActivities.MedicationListActivity;
import pl.chmielewski.medicationcalendar.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

// Creating Listener for MedicationList
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        if (position == 1) {
                            Intent intent = new Intent(OptionsActivity.this,
                                    MedicationListActivity.class);
                            startActivity(intent);
                        }
//                        else if (position == 1)
//                        {
//                            Intent intent = new Intent(OptionsActivity.this,
//                                    DeleteMedicamentActivity.class);
//                            startActivity(intent);
//                        }
                        else if (position == 0)
                        {
                            Intent intent = new Intent(OptionsActivity.this,
                                    AddMedicamentActivity.class);
                            startActivity(intent);
                        }
                    }
                };
        ListView listView =findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }
}
