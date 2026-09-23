package com.rosery.app.model;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static final String PREF = "rosery_products";
    private static final String CUSTOM_PRODUCTS = "custom_products";
    private static final String STOCK_PREF = "rosery_stock";

    public static List<Product> all() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(1, "Organic Rose Extract", "Skincare", "৳ 450", "🌹", "Sylhet Farm", "Pure organic rose extract for glowing skin."));
        list.add(new Product(2, "Fresh Damask Roses", "Flowers", "৳ 300", "🌸", "Gazipur Garden", "Freshly harvested fragrant damask roses."));
        list.add(new Product(3, "Rose Water Mist", "Skincare", "৳ 250", "💧", "Dhaka Lab", "Natural refreshing rose water spray."));
        list.add(new Product(4, "Rose Petal Tea", "Beverage", "৳ 350", "🍵", "Sylhet Estate", "Aromatic herbal tea made from dried rose petals."));
        list.add(new Product(5, "Rose Honey Infusion", "Food", "৳ 550", "🍯", "Sundarbans", "Organic honey infused with real rose petals."));
        list.add(new Product(6, "Rose Essential Oil", "Aromatherapy", "৳ 850", "🌿", "Chittagong Hills", "Pure essential oil for relaxation and skincare."));
        list.add(new Product(7, "Dried Rose Buds", "Crafts", "৳ 200", "🌷", "Rajshahi Farm", "Hand-selected dried rose buds for crafts and tea."));
        list.add(new Product(8, "Rose Body Scrub", "Skincare", "৳ 400", "✨", "Dhaka Lab", "Exfoliating body scrub with rose extracts."));
        return list;
    }

    public static List<Product> all(Context c) {
        List<Product> list = all();
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String data = p.getString(CUSTOM_PRODUCTS, "");
        if (!data.isEmpty()) {
            for (String r : data.split("\\n")) {
                String[] a = r.split("\\|", -1);
                if (a.length >= 7) {
                    try {
                        list.add(new Product(Integer.parseInt(a[0]), a[1], a[2], a[3], a[4], a[5], a[6]));
                    } catch (Exception ignored) {}
                }
            }
        }
        return list;
    }

    public static Product find(int id) {
        for (Product p : all()) {
            if (p.id == id) return p;
        }
        return null;
    }

    public static int getStock(Context c, int id) {
        SharedPreferences p = c.getSharedPreferences(STOCK_PREF, Context.MODE_PRIVATE);
        return p.getInt("stock_" + id, 50);
    }

    public static void setStock(Context c, int id, int stock) {
        SharedPreferences p = c.getSharedPreferences(STOCK_PREF, Context.MODE_PRIVATE);
        p.edit().putInt("stock_" + id, stock).apply();
    }

    public static void addProduct(Context c, String name, String category, String price, String icon, String origin, String description, int stock) {
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        int newId = 100 + all(c).size();
        if (icon == null || icon.trim().isEmpty()) icon = "🌹";
        String row = newId + "|" + name + "|" + category + "|" + price + "|" + icon + "|" + origin + "|" + description;
        String old = p.getString(CUSTOM_PRODUCTS, "");
        p.edit().putString(CUSTOM_PRODUCTS, row + (old.isEmpty() ? "" : "\n" + old)).apply();
        setStock(c, newId, stock);
    }

    public static void deleteProduct(Context c, int id) {
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String data = p.getString(CUSTOM_PRODUCTS, "");
        StringBuilder out = new StringBuilder();
        for (String r : data.split("\\n")) {
            String[] a = r.split("\\|", -1);
            if (a.length >= 7) {
                try {
                    if (Integer.parseInt(a[0]) == id) continue;
                } catch (Exception ignored) {}
            }
            if (out.length() > 0) out.append("\n");
            out.append(r);
        }
        p.edit().putString(CUSTOM_PRODUCTS, out.toString()).apply();
    }
}
