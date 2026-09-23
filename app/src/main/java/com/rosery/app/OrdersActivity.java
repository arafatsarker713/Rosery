package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Customer Order History & Tracking Activity.
 * Displays all past and active orders for the currently authenticated user,
 * including date, items count, financial summary, and live supply tracking link.
 */
public class OrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        findViewById(R.id.ordersBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.trackOrderButton).setOnClickListener(v -> 
            startActivity(new Intent(this, SupplyTrackingActivity.class)));

        TextView orderDisplayBox = findViewById(R.id.emptyOrders);
        Session session = new Session(this);

        List<String[]> userOrders = OrderManager.forUser(this, session.getEmail());
        if (userOrders.isEmpty()) {
            orderDisplayBox.setText("No orders placed yet. Browse products and place your first order!");
        } else {
            StringBuilder builder = new StringBuilder("CUSTOMER ORDER HISTORY\n\n");
            for (String[] order : userOrders) {
                builder.append("Order #").append(order[0])
                       .append("\nDate: ").append(order[1])
                       .append("\nItems: ").append(order[3])
                       .append("   Total: ৳ ").append(order[2])
                       .append("\nPayment: ").append(order[4])
                       .append("\nStatus: ").append(order[7])
                       .append("\nAddress: ").append(order[5])
                       .append("\n----------------------------------------\n\n");
            }
            orderDisplayBox.setText(builder.toString());
        }
    }
}
