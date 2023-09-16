package pl.chmielewski.medicationcalendar.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.chmielewski.medicationcalendar.R;
import pl.chmielewski.medicationcalendar.medicamentsList.RecyclerViewActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView createNewAccount, continueWithoutLogin;
    private EditText inputEmail, inputPassword;
    private Button btnLogin;
    private ImageView btnGoogle;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createNewAccount = findViewById(R.id.alreadyHaveAccountTextView);
        continueWithoutLogin = findViewById(R.id.continueWithoutLoginTextView);
        inputEmail = findViewById(R.id.inputEmailLoginTextView);
        inputPassword = findViewById(R.id.inputPasswordTextView);
        btnLogin = findViewById(R.id.loginButton);
        btnGoogle = findViewById(R.id.googleButton);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        createNewAccount.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
        btnLogin.setOnClickListener(view -> performLogin());
        continueWithoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przenieś użytkownika do aktywności RecyclerShow
                sendUserToNextActivity();
                finish();
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przenieś użytkownika do aktywności RecyclerShow
                Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (firebaseUser != null) {
            sendUserToNextActivity();
        }
    }

    private void performLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            inputEmail.setError("Podaj poprawny email");
        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Podaj poprawne hasło");
        } else {
            progressDialog.setMessage("Logowanie w toku...");
            progressDialog.setTitle("Logowanie");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this, "Logowanie przebiegło pomyślnie", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, RecyclerViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
