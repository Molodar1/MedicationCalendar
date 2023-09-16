package pl.chmielewski.medicationcalendar.medicamentsList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.data.medicament.MedicamentRepository;

public class AddMedicament extends AppCompatActivity {

    private EditText edtMedicamentName, edtMedicamentDose, edtMedicamentAdditionalInfo, edtMedicamentNumberOfDoses;
    private Button sendDatabtn;
    private Button btnBackMedicamentAdd;
    private MedicamentRepository medicamentRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add_medicament);

        // initializing our edittext and button
        edtMedicamentName = findViewById(R.id.editTextInputMedicamentName);
        edtMedicamentDose = findViewById(R.id.editTextInputMedicamentDose);
        edtMedicamentNumberOfDoses = findViewById(R.id.editTextInputMedicamentNumberOfDoses);
        edtMedicamentAdditionalInfo = findViewById(R.id.editTextInputMedicamentAdditionalInfo);

        medicamentRepository = new MedicamentRepository(getApplication());

        sendDatabtn = findViewById(R.id.btnSendMedicamentToFirebase);
        btnBackMedicamentAdd = findViewById(R.id.btnCancelSubmitingMedicament);
        btnBackMedicamentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMedicament.this, RecyclerViewActivity.class));
            }
        });
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String medicamentName = edtMedicamentName.getText().toString();
                String medicamentDose = edtMedicamentDose.getText().toString();
                String medicamentNumberOfDoses = edtMedicamentNumberOfDoses.getText().toString();
                String medicamentAdditionalInfo = edtMedicamentAdditionalInfo.getText().toString();
                if (TextUtils.isEmpty(medicamentName) || TextUtils.isEmpty(medicamentDose)) {
                    Toast.makeText(AddMedicament.this, "Pola z nazwą i dawką leku muszą zostać uzupełnione.", Toast.LENGTH_SHORT).show();
                } else {
                    addDataToLocalDatabase(medicamentName, medicamentDose, medicamentNumberOfDoses, medicamentAdditionalInfo);
                }
            }
        });
    }

    private void addDataToLocalDatabase(String medicamentName, String medicamentDose, String medicamentNumberOfDoses, String medicamentAdditionalInfo) {
        if (TextUtils.isEmpty(medicamentName) || TextUtils.isEmpty(medicamentDose) || TextUtils.isEmpty(medicamentNumberOfDoses)) {
            Toast.makeText(AddMedicament.this, "Wszystkie pola muszą być uzupełnione", Toast.LENGTH_SHORT).show();
        } else {
            Medicament newMedicament = new Medicament(medicamentName, medicamentDose, medicamentAdditionalInfo, Integer.parseInt(medicamentNumberOfDoses));
            String medicamentId = UUID.randomUUID().toString();
            newMedicament.setMedicamentId(medicamentId);

            try {
                medicamentRepository.insert(newMedicament);
                Toast.makeText(AddMedicament.this, "lek został dodany", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(AddMedicament.this, "Wystąpił błąd podczas dodawania leku", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

}