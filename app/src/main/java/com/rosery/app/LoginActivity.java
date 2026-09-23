package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (new Session(this).isLoggedIn()) {
            if (new Session(this).isAdmin()) {
                startActivity(new Intent(this, AdminDashboardActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
            return;
        }

        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);

        findViewById(R.id.loginButton).setOnClickListener(v -> {
            String em = email.getText().toString().trim();
            String pw = password.getText().toString().trim();
            if (em.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] user = UserManager.authenticate(this, em, pw);
            if (user != null) {
                new Session(this).login(user[0], user[1], user[2]);
                if ("admin".equals(user[2])) {
                    startActivity(new Intent(this, AdminDashboardActivity.class));
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials or account blocked", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.gotoRegisterButton).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
