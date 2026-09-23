package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.registerBackButton).setOnClickListener(v -> finish());

        EditText nameInput = findViewById(R.id.nameInput);
        EditText emailInput = findViewById(R.id.registerEmailInput);
        EditText passwordInput = findViewById(R.id.registerPasswordInput);

        findViewById(R.id.registerSubmit).setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = UserManager.register(this, name, email, password);
            if (success) {
                Toast.makeText(this, "Registration successful! Please log in.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Email already exists or invalid", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
