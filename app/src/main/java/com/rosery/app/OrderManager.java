package com.rosery.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderManager {
    private static final String PREF = "rosery_orders";
    private static final String DATA = "data";

    public static String create(Context c, String address, String phone, String payment, int total, int items) {
        String id = "ROS-" + (1000 + (int) (Math.random() * 9000));
        String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(new Date());
        String email = new Session(c).getEmail();
        String row = id + "|" + date + "|" + total + "|" + items + "|" + payment + "|" + clean(address) + "|" + clean(phone) + "|Pending|" + clean(email);
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String old = p.getString(DATA, "");
        p.edit().putString(DATA, row + (old.isEmpty() ? "" : "\n" + old)).apply();
        return id;
    }

    public static List<String[]> all(Context c) {
        return read(c, null);
    }

    public static List<String[]> forUser(Context c, String email) {
        return read(c, email);
    }

    private static List<String[]> read(Context c, String email) {
        List<String[]> list = new ArrayList<>();
        String data = c.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(DATA, "");
        if (data.isEmpty()) return list;
        for (String r : data.split("\\n")) {
            String[] a = r.split("\\|", -1);
            if (a.length >= 9 && (email == null || a[8].equalsIgnoreCase(email))) list.add(a);
        }
        return list;
    }

    public static boolean updateStatus(Context c, String id, String status) {
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String data = p.getString(DATA, "");
        StringBuilder out = new StringBuilder();
        boolean changed = false;
        for (String r : data.split("\\n")) {
            String[] a = r.split("\\|", -1);
            if (a.length >= 9 && a[0].equalsIgnoreCase(id)) {
                a[7] = status;
                r = join(a);
                changed = true;
            }
            if (out.length() > 0) out.append('\n');
            out.append(r);
        }
        if (changed) p.edit().putString(DATA, out.toString()).apply();
        return changed;
    }

    public static int totalSales(Context c) {
        int sum = 0;
        for (String[] a : all(c))
            try {
                if (!"Cancelled".equals(a[7])) sum += Integer.parseInt(a[2]);
            } catch (Exception ignored) {
            }
        return sum;
    }

    private static String clean(String s) {
        return s.replace("|", " ").replace("\n", " ").trim();
    }

    private static String join(String[] a) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (i > 0) s.append('|');
            s.append(a[i]);
        }
        return s.toString();
    }
}
