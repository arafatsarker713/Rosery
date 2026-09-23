package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_admin_dashboard);
        Session s = new Session(this);
        if (!s.isLoggedIn() || !s.isAdmin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        bind();
    }

    private void bind() {
        TextView orders = findViewById(R.id.statOrders);
        TextView users = findViewById(R.id.statUsers);
        TextView products = findViewById(R.id.statProducts);
        TextView sales = findViewById(R.id.statSales);

        orders.setText(String.valueOf(OrderManager.all(this).size()));
        users.setText(String.valueOf(UserManager.all(this).size()));
        products.setText(String.valueOf(com.rosery.app.model.ProductRepository.all().size()));
        sales.setText("৳ " + OrderManager.totalSales(this));

        findViewById(R.id.adminOrdersButton).setOnClickListener(v -> startActivity(new Intent(this, AdminOrdersActivity.class)));
        findViewById(R.id.adminProductsButton).setOnClickListener(v -> startActivity(new Intent(this, AdminProductsActivity.class)));
        findViewById(R.id.adminUsersButton).setOnClickListener(v -> startActivity(new Intent(this, AdminUsersActivity.class)));
        findViewById(R.id.adminLogoutButton).setOnClickListener(v -> {
            new Session(this).logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    protected void onResume() {
        super.onResume();
        if (new Session(this).isAdmin()) bind();
    }
}
