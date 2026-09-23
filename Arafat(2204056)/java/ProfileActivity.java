package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.profileBackButton).setOnClickListener(v -> finish());

        Session session = new Session(this);
        TextView info = findViewById(R.id.profileInfo);
        Button login = findViewById(R.id.profileLoginButton);
        Button logout = findViewById(R.id.logoutButton);

        if (session.isLoggedIn()) {
            info.setText(session.getName() + "\n" + session.getEmail() + "\n\n✓ Verified Rosery Agro Account");
            login.setText("Account is Active");
            login.setEnabled(false);
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(v -> {
                session.logout();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        } else {
            info.setText("Guest User\nBrowse produce & supply tracking freely.\nLogin to place orders.");
            login.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
            logout.setVisibility(View.GONE);
        }
    }
}
