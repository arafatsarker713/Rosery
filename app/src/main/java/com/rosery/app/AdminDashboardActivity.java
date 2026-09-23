package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Administrative Control Center Activity for Rosery Platform.
 * Displays overall marketplace metrics including total orders, registered users,
 * product counts, and revenue totals. Allows navigation to order management,
 * product management, user administration, and secure logout.
 */
public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Verify administrator session before presenting controls
        Session session = new Session(this);
        if (!session.isLoggedIn() || !session.isAdmin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bindDashboardData();
    }

    /**
     * Binds current marketplace statistics and sets up administrative navigation handlers.
     */
    private void bindDashboardData() {
        TextView orders = findViewById(R.id.statOrders);
        TextView users = findViewById(R.id.statUsers);
        TextView products = findViewById(R.id.statProducts);
        TextView sales = findViewById(R.id.statSales);

        // Fetch real-time statistics from dataset managers
        orders.setText(String.valueOf(OrderManager.all(this).size()));
        users.setText(String.valueOf(UserManager.all(this).size()));
        products.setText(String.valueOf(com.rosery.app.model.ProductRepository.all().size()));
        sales.setText("৳ " + OrderManager.totalSales(this));

        // Administrative module navigation listeners
        findViewById(R.id.adminOrdersButton).setOnClickListener(v -> 
            startActivity(new Intent(this, AdminOrdersActivity.class)));
        findViewById(R.id.adminProductsButton).setOnClickListener(v -> 
            startActivity(new Intent(this, AdminProductsActivity.class)));
        findViewById(R.id.adminUsersButton).setOnClickListener(v -> 
            startActivity(new Intent(this, AdminUsersActivity.class)));
        findViewById(R.id.adminLogoutButton).setOnClickListener(v -> {
            new Session(this).logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new Session(this).isAdmin()) {
            bindDashboardData();
        }
    }
}
