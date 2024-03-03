package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView btn;

    private EditText Username, Password, Email, ConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //validation of email

    ProgressDialog ProgressDialog;
    Button btnRegiter;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn = findViewById(R.id.alreadyHaveaccount);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        // hello

        btnRegiter = findViewById(R.id.button);
        ProgressDialog = new ProgressDialog(this);
        btnRegiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCrededentials();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void checkCrededentials() {
        String username = Username.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmpassword = ConfirmPassword.getText().toString();


        if (username.isEmpty() || username.length() < 7) {
            showError(Username, "You username is not valid");
        } else if (!email.matches(emailPattern)) {
            showError(Email, "Enter valid Email");
        } else if (email.isEmpty() || !email.contains("@gmail.com")) {
            showError(Email, "Email is not valid");
        } else if (password.isEmpty() || password.length() < 7) {
            showError(Password, "password must be greater than 7 character");
        } else if (confirmpassword.isEmpty() || !confirmpassword.equals(password)) {
            showError(ConfirmPassword, "Password not match");
        } else {
            ProgressDialog.setMessage("Please wait while Regitration...");
            ProgressDialog.setTitle("Registration");
            ProgressDialog.setCanceledOnTouchOutside(false);
            ProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        ProgressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    } else {
                        ProgressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Already have the account", Toast.LENGTH_LONG).show();
                    }
                }

                private void sendUserToNextActivity() {
                    Intent Intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent);
                }
            });

        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();

    }
}