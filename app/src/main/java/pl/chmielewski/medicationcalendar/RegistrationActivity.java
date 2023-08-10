package pl.chmielewski.medicationcalendar;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    TextView alreadyHaveAccount;
    EditText inputEmail,inputPassword,inputConfirmationPassword;
    Button btnRegister;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        alreadyHaveAccount =findViewById(R.id.alreadyHaveAccountTextView);

        inputEmail=findViewById(R.id.inputEmailRegistrationTextView);
        inputPassword=findViewById(R.id.inputPasswordTextView);
        inputConfirmationPassword=findViewById(R.id.inputConfirmationPasswordTextView);
        btnRegister=findViewById(R.id.registerButton);

        alreadyHaveAccount.setOnClickListener(view -> startActivity(new Intent(RegistrationActivity.this,LoginActivity.class)));
        btnRegister.setOnClickListener(view -> PerformAuthentication());


    }

    private void PerformAuthentication() {
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String confirmPassword=inputConfirmationPassword.getText().toString();
        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)){
            inputEmail.setError("Podaj poprawny email");
        }else if(password.isEmpty() || password.length()<6){
            inputPassword.setError("Podaj poprawne hasło");
        } else if (!password.equals(confirmPassword)) {
            inputConfirmationPassword.setError("Hasła nie są takie same");
        }else {
        progressDialog.setMessage("Rejestracja w toku...");
        progressDialog.setTitle("Rejestracja");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progressDialog.dismiss();
                sendUserToNextActivity();
                Toast.makeText(RegistrationActivity.this,"Rejestracja przebiegła pomyślnie",Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
            }
        });
        }


    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(RegistrationActivity.this, RecylcerShow.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}