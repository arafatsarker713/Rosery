package com.rosery.app;

import com.rosery.app.model.Product;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static final Map<Integer, Integer> cart = new LinkedHashMap<>();

    public static void add(Product product) {
        cart.put(product.id, getQuantity(product) + 1);
    }

    public static void remove(Product product) {
        cart.remove(product.id);
    }

    public static void increase(Product product) {
        add(product);
    }

    public static void decrease(Product product) {
        int q = getQuantity(product);
        if (q <= 1) cart.remove(product.id);
        else cart.put(product.id, q - 1);
    }

    public static int getQuantity(Product product) {
        return cart.containsKey(product.id) ? cart.get(product.id) : 0;
    }

    public static List<Product> getItems() {
        List<Product> result = new ArrayList<>();
        for (Product p : com.rosery.app.model.ProductRepository.all())
            if (cart.containsKey(p.id)) result.add(p);
        return result;
    }

    public static int count() {
        int n = 0;
        for (int q : cart.values()) n += q;
        return n;
    }

    public static int total() {
        int total = 0;
        for (Product p : getItems()) {
            String digits = p.price.replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) total += Integer.parseInt(digits) * getQuantity(p);
        }
        return total;
    }

    public static void clear() {
        cart.clear();
    }
}
