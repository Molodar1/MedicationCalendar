package pl.chmielewski.medicationcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.data.medicament.MedicamentRepository;

public class AddMedicament extends AppCompatActivity {

    // creating variables for
    // EditText and buttons.
    private EditText edtMedicamentName, edtMedicamentDose, edtMedicamentAdditionalInfo,edtMedicamentNumberOfDoses;
    private Button sendDatabtn;
    private Button btnBackMedicineAdd;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;
    private MedicamentRepository medicamentRepository;


    // creating a variable for
    // our object class
    Medicament medicament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add_medicine);

        // initializing our edittext and button
        edtMedicamentName = findViewById(R.id.editTextInputMedicineName);
        edtMedicamentDose = findViewById(R.id.editTextInputMedicineDose);
        edtMedicamentNumberOfDoses=findViewById(R.id.editTextInputMedicineNumberOfDoses);
        edtMedicamentAdditionalInfo = findViewById(R.id.editTextInputMedicamentAdditionalInfo);
        // below line is used to get the
        // instance of our FIrebase database.
        medicamentRepository = new MedicamentRepository(getApplication());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Medicament")
                    .child(userId);
        }

        // below line is used to get reference for our database.
        //databaseReference = firebaseDatabase.getReference("Medicament");

        // initializing our object
        // class variable.
        medicament = new Medicament();

        sendDatabtn = findViewById(R.id.btnSendMedicamentToFirebase);
        btnBackMedicineAdd = findViewById(R.id.btnCancelSubmitingMedicament);
        btnBackMedicineAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddMedicament.this, RecyclerShow.class));
            }
        });
        // adding on click listener for our button.

        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String medicamentName = edtMedicamentName.getText().toString();
                String medicamentDose = edtMedicamentDose.getText().toString();
                String medicamentNumberOfDoses=edtMedicamentNumberOfDoses.getText().toString();
                String medicamentAdditionalInfo = edtMedicamentAdditionalInfo.getText().toString();
                // String cost =  //Double.parseDouble(costString);
                if (TextUtils.isEmpty(medicamentName) || TextUtils.isEmpty(medicamentDose) ) {
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