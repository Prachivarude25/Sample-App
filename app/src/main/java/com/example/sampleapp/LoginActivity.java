package com.example.sampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.PasswordAuthentication;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView btn,forgotPassword;
    EditText Email,Password;
    String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    android.app.ProgressDialog ProgressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn=findViewById(R.id.textViewsignUp);
        Email=findViewById(R.id.Email);
        Password=findViewById(R.id.Password);
        btnLogin=findViewById(R.id.button2);
        forgotPassword=findViewById(R.id.forgotPassword);
        ProgressDialog=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCrededentials();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });


    }

    private void checkCrededentials() {
        String email=Email.getText().toString();
        String password=Password.getText().toString();

        if (email.isEmpty() || !email.contains("@gmail.com"))
        {
            showError(Email,"Email is not valid");
        }
        else if (!email.matches(emailPattern))
        {
            showError(Email,"Enter valid Email");
        }
        else if (password.isEmpty() || password.length()<7)
        {
            showError(Password, "password must be greater than 7 character");
        }
        else
        {
            ProgressDialog.setMessage("Please wait while Login...");
            ProgressDialog.setTitle("Login");
            ProgressDialog.setCanceledOnTouchOutside(false);
            ProgressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        ProgressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_LONG).show();
                    }
                    else {
                        ProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Enter valid credential", Toast.LENGTH_LONG).show();
                    }
                }

                private void sendUserToNextActivity(){
                    Intent Intent=new Intent(LoginActivity.this,CreateNoteActivity.class);
                    Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent);
                }
            });

        }

    }

    private void showError(EditText input, String s)
    {
        input.setError(s);
        input.requestFocus();
    }
}