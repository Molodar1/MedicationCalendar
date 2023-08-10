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

import java.util.HashMap;

public class AddMedicament extends AppCompatActivity {

    // creating variables for
    // EditText and buttons.
    private EditText medicineNameEdt, medicineCostEdt, medicineKindEdt,medicineInsideEdt,medicineBoxesEdt;
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
        medicineNameEdt = findViewById(R.id.editTextInputMedicineName);
        medicineKindEdt = findViewById(R.id.editTextInputMedicineDose);
        medicineCostEdt = findViewById(R.id.editTextInputMedicamentDailyFrequency);
        medicineInsideEdt = findViewById(R.id.editTextInputMedicamentDoseUnit);
        medicineBoxesEdt = findViewById(R.id.editTextInputMedicamentAdditionalInfo);
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
                startActivity(new Intent(AddMedicament.this, RecylcerShow.class));
            }
        });
        // adding on click listener for our button.

        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting text from our edittext fields.
                String name = medicineNameEdt.getText().toString();
                String kind = medicineKindEdt.getText().toString();
                String inside = medicineInsideEdt.getText().toString();
                String cost = medicineCostEdt.getText().toString();//String
                int box = Integer.parseInt(medicineBoxesEdt.getText().toString());
                // String cost =  //Double.parseDouble(costString);
                if (!TextUtils.isEmpty(name) && !Character.isUpperCase(name.charAt(0))) {
                    Toast.makeText(AddMedicament.this, "Nazwa leku musi zaczynać się dużą literą.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(kind) || TextUtils.isEmpty(cost) || TextUtils.isEmpty(inside) || TextUtils.isEmpty(medicineBoxesEdt.getText().toString())) {
                    Toast.makeText(AddMedicament.this, "Proszę uzupełnić wszystkie pola.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //  else call the method to add data to our database.

                    addDatatoFirebase(name, kind, inside, cost, box);
                }
            }
        });
    }

    private void addDatatoFirebase(String name, String kind, String inside, String  cost,int box) {
        // below 3 lines of code is used to set
        // data in our object class.
        HashMap<String, Object> medicineHashmap = new HashMap<>();
        //  String medicineId = databaseReference.push().getKey();
        medicament.setMedicamentName(name);
        medicament.setMedicamentFrequencyTimeMeasure(kind);
        medicament.setMedicamentDose(Double.parseDouble(inside));
        medicament.setMedicamentAdditionalInfo(cost);
        medicament.setMedicamentDosingFrequency(box);
        String key = databaseReference.push().getKey();
        //medicineHashmap.put("key",key);
        //medicineHashmap.put( key,medicineInfo);
        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.child(key).setValue(medicament).addOnCompleteListener(new OnCompleteListener<Void>() {
  @Override
  public void onComplete(@NonNull Task<Void> task) {
      Toast.makeText(AddMedicament.this, "lek dodany", Toast.LENGTH_SHORT).show();
      medicineNameEdt.getText().clear();
      medicineKindEdt.getText().clear();
      medicineInsideEdt.getText().clear();
      medicineCostEdt.getText().clear();
      medicineBoxesEdt.getText().clear();
  }
}
        );
    }
}