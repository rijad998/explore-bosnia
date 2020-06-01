package com.example.sparklingsmiledentalclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText emailLogEditText;
    EditText passwordLogEditText;
    Button loginLogButton;
    ProgressBar progressBarLogin;
    TextView goToRegisterActivity;

    private FirebaseAuth logAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogEditText = findViewById(R.id.emailLogEditText);
        passwordLogEditText = findViewById(R.id.passwordLogEditText);
        loginLogButton = findViewById(R.id.loginLogBtn);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.GONE);
        goToRegisterActivity = findViewById(R.id.loginToRegisterTextView);

        logAuth = FirebaseAuth.getInstance();

        loginLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        goToRegisterActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    public void loginUser(){
        progressBarLogin.setVisibility(View.VISIBLE);
        final String email = emailLogEditText.getText().toString().trim();
        final String password = passwordLogEditText.getText().toString().trim();

        logAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressBarLogin.setVisibility(View.GONE);
                    startActivity(new Intent(LoginActivity.this, UserFragmentActivity.class));
                    finish();
                } else {
                    progressBarLogin.setVisibility(View.GONE);
                    passwordLogEditText.getText().clear();
                    Toast.makeText(LoginActivity.this, "Invalid username or password!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}