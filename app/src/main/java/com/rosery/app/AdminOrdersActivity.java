package com.rosery.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

/**
 * Administrative Order Management Activity.
 * Allows store administrators to view customer orders, inspect payment & contact details,
 * and dynamically update delivery status (Pending, Confirmed, Shipped, Delivered, etc.).
 */
public class AdminOrdersActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private final String[] statuses = {"Pending", "Confirmed", "Processing", "Shipped", "Delivered", "Cancelled"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        TextView title = findViewById(R.id.adminListTitle);
        title.setText("Manage Orders");

        findViewById(R.id.adminListBack).setOnClickListener(v -> finish());
        listContainer = findViewById(R.id.adminListContainer);

        renderOrders();
    }

    /**
     * Renders order cards with detailed customer, product, and status information.
     */
    void renderOrders() {
        listContainer.removeAllViews();
        List<String[]> orders = OrderManager.all(this);

        if (orders.isEmpty()) {
            addTextMessage("No orders placed yet.");
            return;
        }

        for (String[] order : orders) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(20, 18, 20, 18);

            TextView info = new TextView(this);
            info.setText("Order #" + order[0] + "\nCustomer: " + order[8] + "\nDate: " + order[1] + 
                         "\nItems: " + order[3] + "   Total: ৳ " + order[2] + "\nPayment: " + order[4] + 
                         "\nPhone: " + order[6] + "\nAddress: " + order[5] + "\nStatus: " + order[7]);
            info.setTextSize(14);
            card.addView(info);

            Button updateStatusBtn = new Button(this);
            updateStatusBtn.setText("Update Status");
            updateStatusBtn.setOnClickListener(v -> promptStatusChange(order[0]));
            card.addView(updateStatusBtn);

            listContainer.addView(card, new LinearLayout.LayoutParams(-1, -2));

            Space spacer = new Space(this);
            listContainer.addView(spacer, new LinearLayout.LayoutParams(1, 14));
        }
    }

    /**
     * Displays a empty message state in the container list.
     */
    private void addTextMessage(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setPadding(16, 24, 16, 24);
        listContainer.addView(textView);
    }

    /**
     * Shows dialog to select new status for the target order ID.
     */
    private void promptStatusChange(String orderId) {
        new AlertDialog.Builder(this)
                .setTitle("Update Order #" + orderId)
                .setItems(statuses, (dialog, which) -> {
                    OrderManager.updateStatus(this, orderId, statuses[which]);
                    Toast.makeText(this, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                    renderOrders();
                })
                .show();
    }
}
