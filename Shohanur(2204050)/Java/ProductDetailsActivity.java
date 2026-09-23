package com.rosery.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.rosery.app.model.Product;
import com.rosery.app.model.ProductRepository;

public class ProductDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        int id = getIntent().getIntExtra("id", 1);
        Product selected = null;
        for (Product p : ProductRepository.all(this)) {
            if (p.id == id) {
                selected = p;
                break;
            }
        }
        if (selected == null) {
            finish();
            return;
        }

        Product p = selected;
        ((TextView)findViewById(R.id.detailIcon)).setText(p.icon);
        ((TextView)findViewById(R.id.detailName)).setText(p.name);
        ((TextView)findViewById(R.id.detailPrice)).setText(p.price);
        ((TextView)findViewById(R.id.detailDescription)).setText(p.description);
        ((TextView)findViewById(R.id.detailTracking)).setText(
                "✓ Origin: " + p.origin +
                "\n✓ Category: " + p.category +
                "\n✓ Inspection: Passed Quality Control" +
                "\n✓ Supply Route: Verified Farm → Rosery Hub → Buyer");

        Button add = findViewById(R.id.addToCartButton);
        add.setOnClickListener(v -> {
            CartManager.add(p);
            Toast.makeText(this, p.name + " added to your cart", Toast.LENGTH_SHORT).show();
        });
    }
}
