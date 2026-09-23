package com.rosery.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.rosery.app.model.Product;
import com.rosery.app.model.ProductRepository;
import java.util.List;

public class AdminProductsActivity extends AppCompatActivity {
    LinearLayout list;

    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_admin_list);
        ((TextView) findViewById(R.id.adminListTitle)).setText("Manage Products & Stock");
        findViewById(R.id.adminListBack).setOnClickListener(v -> finish());
        list = findViewById(R.id.adminListContainer);
        render();
    }

    void render() {
        list.removeAllViews();
        Button add = new Button(this);
        add.setText("+ Add New Product");
        add.setOnClickListener(v -> addDialog());
        list.addView(add);
        List<Product> ps = ProductRepository.all(this);
        for (Product p : ps) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(20, 18, 20, 18);
            TextView t = new TextView(this);
            t.setText(p.icon + "  " + p.name + "\n" + p.category + "\nPrice: " + p.price + "\nStock: " + ProductRepository.getStock(this, p.id) + " units");
            t.setTextSize(15);
            card.addView(t);
            Button b = new Button(this);
            b.setText("Update Stock");
            b.setOnClickListener(v -> stockDialog(p));
            card.addView(b);
            if (p.id > 8) {
                Button del = new Button(this);
                del.setText("Delete Product");
                del.setOnClickListener(v -> {
                    ProductRepository.deleteProduct(this, p.id);
                    render();
                });
                card.addView(del);
            }
            list.addView(card, new LinearLayout.LayoutParams(-1, -2));
            Space sp = new Space(this);
            list.addView(sp, new LinearLayout.LayoutParams(1, 14));
        }
    }

    void addDialog() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(24, 0, 24, 0);
        EditText n = new EditText(this);
        n.setHint("Product name");
        EditText c = new EditText(this);
        c.setHint("Category");
        EditText pr = new EditText(this);
        pr.setHint("Price e.g. ৳ 100 / kg");
        EditText ic = new EditText(this);
        ic.setHint("Icon e.g. 🌹");
        EditText o = new EditText(this);
        o.setHint("Farm origin");
        EditText d = new EditText(this);
        d.setHint("Description");
        EditText st = new EditText(this);
        st.setHint("Opening stock");
        box.addView(n);
        box.addView(c);
        box.addView(pr);
        box.addView(ic);
        box.addView(o);
        box.addView(d);
        box.addView(st);
        new AlertDialog.Builder(this).setTitle("Add Product").setView(box).setNegativeButton("Cancel", null).setPositiveButton("Save", (x, w) -> {
            if (n.getText().toString().trim().isEmpty() || pr.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Name and price are required", Toast.LENGTH_SHORT).show();
                return;
            }
            int stock = 50;
            try {
                stock = Integer.parseInt(st.getText().toString());
            } catch (Exception ignored) {
            }
            ProductRepository.addProduct(this, n.getText().toString(), c.getText().toString(), pr.getText().toString(), ic.getText().toString(), o.getText().toString(), d.getText().toString(), stock);
            render();
        }).show();
    }

    void stockDialog(Product p) {
        final EditText input = new EditText(this);
        input.setText(String.valueOf(ProductRepository.getStock(this, p.id)));
        new AlertDialog.Builder(this).setTitle("Stock for " + p.name).setView(input).setNegativeButton("Cancel", null).setPositiveButton("Save", (d, w) -> {
            try {
                ProductRepository.setStock(this, p.id, Integer.parseInt(input.getText().toString()));
                render();
            } catch (Exception e) {
                Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}
