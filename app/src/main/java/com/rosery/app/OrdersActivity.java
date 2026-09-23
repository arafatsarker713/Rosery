package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_orders);
        findViewById(R.id.ordersBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.trackOrderButton).setOnClickListener(v -> startActivity(new Intent(this, SupplyTrackingActivity.class)));

        TextView box = findViewById(R.id.emptyOrders);
        List<String[]> orders = OrderManager.forUser(this, new Session(this).getEmail());
        if (orders.isEmpty()) {
            box.setText("No orders yet. Add products to your cart and checkout to create your first order.");
        } else {
            StringBuilder s = new StringBuilder("ORDER HISTORY\n\n");
            for (String[] o : orders) {
                s.append("Order #").append(o[0]).append("\nDate: ").append(o[1]).append("\nItems: ").append(o[3]).append("   Total: ৳ ").append(o[2]).append("\nPayment: ").append(o[4]).append("\nStatus: ").append(o[7]).append("\nAddress: ").append(o[5]).append("\n\n");
            }
            box.setText(s.toString());
        }
    }
}
