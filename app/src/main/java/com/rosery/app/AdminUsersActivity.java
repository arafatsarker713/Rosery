package com.rosery.app;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Administrative User Administration Activity.
 * Enables administrators to manage registered customer accounts, inspect roles,
 * and toggle account statuses between active and blocked states.
 */
public class AdminUsersActivity extends AppCompatActivity {

    private LinearLayout listContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        TextView title = findViewById(R.id.adminListTitle);
        title.setText("Manage Customers");

        findViewById(R.id.adminListBack).setOnClickListener(v -> finish());
        listContainer = findViewById(R.id.adminListContainer);

        renderUsers();
    }

    /**
     * Renders registered user account cards with activation/blocking capabilities.
     */
    private void renderUsers() {
        listContainer.removeAllViews();
        List<String[]> users = UserManager.all(this);

        if (users.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No registered customers found.");
            emptyText.setPadding(16, 24, 16, 24);
            listContainer.addView(emptyText);
            return;
        }

        for (String[] user : users) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(20, 18, 20, 18);

            TextView infoText = new TextView(this);
            infoText.setText("Name: " + user[0] + "\nEmail: " + user[1] + "\nRole: " + user[3] + "\nStatus: " + user[4]);
            infoText.setTextSize(15);
            card.addView(infoText);

            Button actionButton = new Button(this);
            actionButton.setText("active".equals(user[4]) ? "Block Account" : "Activate Account");
            actionButton.setOnClickListener(v -> {
                String newStatus = "active".equals(user[4]) ? "blocked" : "active";
                UserManager.setStatus(this, user[1], newStatus);
                Toast.makeText(this, "User status changed to " + newStatus, Toast.LENGTH_SHORT).show();
                renderUsers();
            });
            card.addView(actionButton);

            listContainer.addView(card);
            Space space = new Space(this);
            listContainer.addView(space, new LinearLayout.LayoutParams(1, 14));
        }
    }
}
