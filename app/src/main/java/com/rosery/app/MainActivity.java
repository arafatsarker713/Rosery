package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Session session = new Session(this);
        TextView greeting = findViewById(R.id.greeting);
        if (session.isLoggedIn()) {
            greeting.setText("Welcome, " + session.getName());
            ((android.widget.Button) findViewById(R.id.loginButton)).setText("Profile");
            findViewById(R.id.loginButton).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        } else {
            greeting.setText("Farmers and buyers together");
            findViewById(R.id.loginButton).setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        }

        findViewById(R.id.trackButton).setOnClickListener(v -> startActivity(new Intent(this, SupplyTrackingActivity.class)));
        findViewById(R.id.ordersButton).setOnClickListener(v -> {
            if (!session.isLoggedIn()) {
                Toast.makeText(this, "Please log in to view orders", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, OrdersActivity.class));
            }
        });
        findViewById(R.id.profileButton).setOnClickListener(v -> {
            if (!session.isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, ProfileActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = new Session(this);
        if (session.isLoggedIn()) {
            ((android.widget.Button) findViewById(R.id.loginButton)).setText("Profile");
            findViewById(R.id.loginButton).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        }
    }
}
