package com.example.sparklingsmiledentalclinic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Button registerProceed;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerProceed = findViewById(R.id.registerProceedBtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        try {
            registerProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String phone = phoneEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.setError("Enter a valid name.");
            nameEditText.requestFocus();
            return;
        }

        if (phone.isEmpty() || phone.length() < 9) {
            phoneEditText.setError("Enter a valid phone number.");
            phoneEditText.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid e-mail address.");
            emailEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters.");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    User user = new User(name, email, phone);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, InsideActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Adding additional data besides email and password failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
