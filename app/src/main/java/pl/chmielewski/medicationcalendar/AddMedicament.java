package pl.chmielewski.medicationcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMedicament extends AppCompatActivity {

    // creating variables for
    // EditText and buttons.
    private EditText edtMedicamentName, edtMedicamentDose, edtMedicamentAdditionalInfo;
    private Button sendDatabtn;
    private Button btnBackMedicineAdd;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

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
        edtMedicamentAdditionalInfo = findViewById(R.id.editTextInputMedicamentAdditionalInfo);
        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("Medicament");

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
                String medicamentAdditionalInfo = edtMedicamentAdditionalInfo.getText().toString();
                // String cost =  //Double.parseDouble(costString);
                if (TextUtils.isEmpty(medicamentName) || TextUtils.isEmpty(medicamentDose) ) {
                    Toast.makeText(AddMedicament.this, "Pola z nazwą i dawką leku muszą zostać uzupełnione.", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(medicamentName, medicamentDose, medicamentAdditionalInfo);
                }
            }
        });
    }

    private void addDatatoFirebase(String medicamentName, String medicamentDose, String medicamentAdditionalInfo) {
        // below 3 lines of code is used to set
        // data in our object class.
       // HashMap<String, Object> medicineHashmap = new HashMap<>();
        //  String medicineId = databaseReference.push().getKey();
        String medicamentId = databaseReference.push().getKey();
        medicament.setMedicamentName(medicamentName);
        medicament.setMedicamentDose(medicamentDose);
        medicament.setMedicamentAdditionalInfo(medicamentAdditionalInfo);

        //medicineHashmap.put("medicamentId",medicamentId);
        //medicineHashmap.put( medicamentId,medicineInfo);
        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.child(medicamentId).setValue(medicament).addOnCompleteListener(new OnCompleteListener<Void>() {
  @Override
  public void onComplete(@NonNull Task<Void> task) {
      Toast.makeText(AddMedicament.this, "lek dodany", Toast.LENGTH_SHORT).show();
      edtMedicamentName.getText().clear();
      edtMedicamentDose.getText().clear();
      edtMedicamentAdditionalInfo.getText().clear();
  }
}
        );
    }
}