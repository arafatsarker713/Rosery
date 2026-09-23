package com.rosery.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {
    LinearLayout list;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_admin_list);
        ((TextView) findViewById(R.id.adminListTitle)).setText("Manage Customers");
        findViewById(R.id.adminListBack).setOnClickListener(v -> finish());
        list = findViewById(R.id.adminListContainer);
        render();
    }

    void render() {
        list.removeAllViews();
        List<String[]> us = UserManager.all(this);
        if (us.isEmpty()) {
            TextView t = new TextView(this);
            t.setText("No registered customers yet.");
            list.addView(t);
            return;
        }
        for (String[] u : us) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(20, 18, 20, 18);
            TextView t = new TextView(this);
            t.setText("Name: " + u[0] + "\nEmail: " + u[1] + "\nRole: " + u[3] + "\nStatus: " + u[4]);
            t.setTextSize(15);
            card.addView(t);
            Button b = new Button(this);
            b.setText("active".equals(u[4]) ? "Block User" : "Activate User");
            b.setOnClickListener(v -> {
                UserManager.setStatus(this, u[1], "active".equals(u[4]) ? "blocked" : "active");
                render();
            });
            card.addView(b);
            list.addView(card);
            Space sp = new Space(this);
            list.addView(sp, new LinearLayout.LayoutParams(1, 14));
        }
    }
}
