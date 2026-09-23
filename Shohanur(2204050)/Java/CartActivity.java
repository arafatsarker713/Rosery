package com.rosery.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rosery.app.adapter.CartAdapter;

public class CartActivity extends AppCompatActivity {
    private TextView total;
    private View emptyCartState;
    private View summaryCard;
    private Button checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        findViewById(R.id.cartBackButton).setOnClickListener(v -> finish());
        findViewById(R.id.browseProduceButton).setOnClickListener(v -> finish());

        total = findViewById(R.id.cartTotal);
        emptyCartState = findViewById(R.id.emptyCartState);
        summaryCard = findViewById(R.id.summaryCard);
        checkout = findViewById(R.id.checkoutButton);

        RecyclerView recycler = findViewById(R.id.cartRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        CartAdapter adapter = new CartAdapter(CartManager.getItems(), this::refreshTotal);
        recycler.setAdapter(adapter);
        refreshTotal();

        checkout.setOnClickListener(v -> {
            if (CartManager.count() == 0) return;
            if (!new Session(this).isLoggedIn()) {
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                startActivity(new Intent(this, CheckoutActivity.class));
            }
        });
    }

    private void refreshTotal() {
        int count = CartManager.count();
        if (count == 0) {
            if (emptyCartState != null) emptyCartState.setVisibility(View.VISIBLE);
            if (summaryCard != null) summaryCard.setVisibility(View.GONE);
            if (checkout != null) checkout.setVisibility(View.GONE);
        } else {
            if (emptyCartState != null) emptyCartState.setVisibility(View.GONE);
            if (summaryCard != null) summaryCard.setVisibility(View.VISIBLE);
            if (checkout != null) checkout.setVisibility(View.VISIBLE);
            total.setText("৳ " + CartManager.total());
        }
    }
}
