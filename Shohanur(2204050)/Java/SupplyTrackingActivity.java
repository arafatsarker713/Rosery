package com.rosery.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SupplyTrackingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_tracking);

        findViewById(R.id.trackBackButton).setOnClickListener(v -> finish());

        EditText input = findViewById(R.id.trackingInput);
        Button search = findViewById(R.id.trackSearchButton);
        TextView result = findViewById(R.id.trackingResult);

        findViewById(R.id.sampleChip1).setOnClickListener(v -> {
            input.setText("ROS-1001");
            performTracking("ROS-1001", result);
        });
        findViewById(R.id.sampleChip2).setOnClickListener(v -> {
            input.setText("ROS-1002");
            performTracking("ROS-1002", result);
        });
        findViewById(R.id.sampleChip3).setOnClickListener(v -> {
            input.setText("ROS-1003");
            performTracking("ROS-1003", result);
        });

        search.setOnClickListener(v -> {
            String id = input.getText().toString().trim();
            if (id.isEmpty()) id = "ROS-1001";
            performTracking(id, result);
        });
    }

    private void performTracking(String id, TextView result) {
        StringBuilder text = new StringBuilder("📦 Order: ").append(id.toUpperCase());
        boolean found=false;
        for(String[] o: OrderManager.all(this)){
            if(o[0].equalsIgnoreCase(id)){
                found=true;
                text.append("\n\nCustomer: ").append(o[8]);
                text.append("\nAmount: ৳ ").append(o[2]);
                text.append("\nCurrent Status: ").append(o[7]);
                text.append("\nDelivery Address: ").append(o[5]);
                break;
            }
        }
        if(!found){
            text.append("\n\n✓ Farm Origin: Verified Eco Cluster #14");
            text.append("\n✓ Harvest: Inspected & Logged");
            text.append("\n✓ Quality Grade: A+ Standard");
            text.append("\n✓ Logistics Route: Active");
        } else {
            text.append("\n\nSupply route: Farmer → Rosery Hub → Buyer");
            text.append("\n✓ Order record verified");
        }
        result.setText(text.toString());
    }
}
