package com.rosery.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.rosery.app.adapter.ProductAdapter;
import com.rosery.app.model.Product;
import com.rosery.app.model.ProductRepository;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProductAdapter adapter;
    private TextView resultTitle;
    private TextView productCountBadge;
    private View emptyStateLayout;
    private EditText search;
    private ImageView searchClearButton;
    private TextView cartBadgeCount;
    private TextView navCartBadge;
    private String currentCategory = "All";

    private MaterialButton chipAll;
    private MaterialButton chipCrops;
    private MaterialButton chipDirectSales;
    private MaterialButton chipGlobalMarket;
    private MaterialButton chipSupply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Session session = new Session(this);
        TextView greeting = findViewById(R.id.greeting);
        MaterialButton login = findViewById(R.id.loginButton);
        search = findViewById(R.id.searchBox);
        searchClearButton = findViewById(R.id.searchClearButton);
        resultTitle = findViewById(R.id.resultTitle);
        productCountBadge = findViewById(R.id.productCountBadge);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        cartBadgeCount = findViewById(R.id.cartBadgeCount);
        navCartBadge = findViewById(R.id.navCartBadge);

        // Header Greeting & Login state
        updateUserState(session, greeting, login);

        // Category Chips setup
        chipAll = findViewById(R.id.categoryAll);
        chipCrops = findViewById(R.id.cropsCategory);
        chipDirectSales = findViewById(R.id.directSalesCategory);
        chipGlobalMarket = findViewById(R.id.globalMarketCategory);
        chipSupply = findViewById(R.id.supplyCategory);

        // RecyclerView Setup
        RecyclerView recycler = findViewById(R.id.productsRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        List<Product> allProducts = ProductRepository.all(this);
        adapter = new ProductAdapter(allProducts, this::openProduct);
        recycler.setAdapter(adapter);
        updateProductCount(allProducts.size());

        // Header Cart Button
        findViewById(R.id.headerCartButton).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        // Login / Profile button
        login.setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        // Hero Banner CTA
        findViewById(R.id.trackButton).setOnClickListener(v ->
                startActivity(new Intent(this, SupplyTrackingActivity.class)));

        // Quick Action Cards
        findViewById(R.id.actionDirectSales).setOnClickListener(v -> selectCategory("Direct Sales", chipDirectSales));
        findViewById(R.id.actionSupplyTracking).setOnClickListener(v -> startActivity(new Intent(this, SupplyTrackingActivity.class)));
        findViewById(R.id.actionGlobalMarket).setOnClickListener(v -> selectCategory("Global Market", chipGlobalMarket));
        findViewById(R.id.actionFreshCrops).setOnClickListener(v -> selectCategory("Fresh Crops", chipCrops));

        // Category Chips Click Listeners
        chipAll.setOnClickListener(v -> selectCategory("All", chipAll));
        chipCrops.setOnClickListener(v -> selectCategory("Fresh Crops", chipCrops));
        chipDirectSales.setOnClickListener(v -> selectCategory("Direct Sales", chipDirectSales));
        chipGlobalMarket.setOnClickListener(v -> selectCategory("Global Market", chipGlobalMarket));
        chipSupply.setOnClickListener(v -> selectCategory("Supply Tracking", chipSupply));

        // Empty state reset button
        findViewById(R.id.resetFilterButton).setOnClickListener(v -> {
            search.setText("");
            selectCategory("All", chipAll);
        });

        // Search text watcher & clear button
        searchClearButton.setOnClickListener(v -> search.setText(""));
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String q = s.toString();
                searchClearButton.setVisibility(q.isEmpty() ? View.GONE : View.VISIBLE);
                applyFilter();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Bottom Navigation Bar Listeners
        findViewById(R.id.homeButton).setOnClickListener(v -> {
            findViewById(R.id.mainScrollView).scrollTo(0, 0);
        });

        findViewById(R.id.navTrackButton).setOnClickListener(v ->
                startActivity(new Intent(this, SupplyTrackingActivity.class)));

        findViewById(R.id.navCartButton).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        findViewById(R.id.ordersButton).setOnClickListener(v ->
                requireLogin(OrdersActivity.class));

        findViewById(R.id.profileButton).setOnClickListener(v -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        updateCartBadges();
    }

    private void updateUserState(Session session, TextView greeting, MaterialButton login) {
        if (session.isLoggedIn()) {
            greeting.setText("Welcome, " + session.getName());
            login.setText("Profile");
        } else {
            greeting.setText("Direct Farm-to-Buyer Market");
            login.setText("Login");
        }
    }

    private void selectCategory(String category, MaterialButton selectedChip) {
        currentCategory = category;
        highlightChip(selectedChip);
        applyFilter();
    }

    private void highlightChip(MaterialButton activeChip) {
        MaterialButton[] chips = {chipAll, chipCrops, chipDirectSales, chipGlobalMarket, chipSupply};
        int activeBg = ContextCompat.getColor(this, R.color.rosery_green);
        int activeText = ContextCompat.getColor(this, R.color.white);
        int inactiveText = ContextCompat.getColor(this, R.color.rosery_green);
        int strokeColor = ContextCompat.getColor(this, R.color.rosery_green);

        for (MaterialButton chip : chips) {
            if (chip == activeChip) {
                chip.setBackgroundTintList(ColorStateList.valueOf(activeBg));
                chip.setTextColor(activeText);
                chip.setStrokeWidth(0);
            } else {
                chip.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.rosery_surface)));
                chip.setTextColor(inactiveText);
                chip.setStrokeColor(ColorStateList.valueOf(strokeColor));
                chip.setStrokeWidth((int) (1.2f * getResources().getDisplayMetrics().density));
            }
        }
    }

    private void applyFilter() {
        String q = search.getText().toString().trim();
        List<Product> baseList;
        if ("All".equalsIgnoreCase(currentCategory)) {
            baseList = ProductRepository.all(this);
        } else {
            baseList = ProductRepository.byCategory(this, currentCategory);
        }

        List<Product> filtered;
        if (!q.isEmpty()) {
            filtered = ProductRepository.search(this, q);
            resultTitle.setText("Search: \"" + q + "\"");
        } else {
            filtered = baseList;
            resultTitle.setText("All".equalsIgnoreCase(currentCategory) ? "Featured Produce" : currentCategory);
        }

        adapter.update(filtered);
        updateProductCount(filtered.size());

        if (filtered.isEmpty()) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void updateProductCount(int count) {
        if (productCountBadge != null) {
            productCountBadge.setText(count + " " + (count == 1 ? "item" : "items"));
        }
    }

    private void updateCartBadges() {
        int count = CartManager.count();
        if (count > 0) {
            String text = String.valueOf(count);
            if (cartBadgeCount != null) {
                cartBadgeCount.setText(text);
                cartBadgeCount.setVisibility(View.VISIBLE);
            }
            if (navCartBadge != null) {
                navCartBadge.setText(text);
                navCartBadge.setVisibility(View.VISIBLE);
            }
        } else {
            if (cartBadgeCount != null) cartBadgeCount.setVisibility(View.GONE);
            if (navCartBadge != null) navCartBadge.setVisibility(View.GONE);
        }
    }

    private void openProduct(Product product) {
        Intent i = new Intent(this, ProductDetailsActivity.class);
        i.putExtra("id", product.id);
        startActivity(i);
    }

    private void requireLogin(Class<?> target) {
        if (!new Session(this).isLoggedIn()) {
            Toast.makeText(this, "Please login to view your orders.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, target));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = new Session(this);
        TextView greeting = findViewById(R.id.greeting);
        MaterialButton login = findViewById(R.id.loginButton);
        updateUserState(session, greeting, login);
        updateCartBadges();
    }
}
