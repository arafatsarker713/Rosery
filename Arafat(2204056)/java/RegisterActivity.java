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

        EditText name = findViewById(R.id.nameInput);
        EditText email = findViewById(R.id.registerEmailInput);
        EditText password = findViewById(R.id.registerPasswordInput);

        findViewById(R.id.registerSubmit).setOnClickListener(v -> {
            String n = name.getText().toString().trim();
            String e = email.getText().toString().trim();
            String p = password.getText().toString();

            if (n.isEmpty() || e.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please fill in all registration fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!UserManager.register(this, n, e, p)) {
                Toast.makeText(this, "Email already registered.", Toast.LENGTH_SHORT).show();
                return;
            }
            new Session(this).login(n, e, "user");
            Toast.makeText(this, "Account created successfully! Welcome, " + n, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
