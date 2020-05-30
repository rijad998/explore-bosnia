package com.example.sparklingsmiledentalclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText phone;
    EditText email;
    EditText password;
    Button registerProceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.nameEditText);
        phone = findViewById(R.id.phoneEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        registerProceed = findViewById(R.id.registerProceedBtn);
    }
}
