package com.rosery.app;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static final String PREF = "rosery_users";
    private static final String DATA = "data";
    private static final String ADMIN_EMAIL = "admin@rosery.com";
    private static final String ADMIN_PASSWORD = "admin123";

    public static boolean register(Context c, String name, String email, String password) {
        if (email.equalsIgnoreCase(ADMIN_EMAIL) || find(c, email) != null) return false;
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String row = clean(name) + "|" + clean(email) + "|" + password + "|user|active";
        String old = p.getString(DATA, "");
        p.edit().putString(DATA, row + (old.isEmpty() ? "" : "\n" + old)).apply();
        return true;
    }

    public static String[] authenticate(Context c, String email, String password) {
        if (ADMIN_EMAIL.equalsIgnoreCase(email) && ADMIN_PASSWORD.equals(password))
            return new String[]{"Administrator", ADMIN_EMAIL, "admin"};
        String[] u = find(c, email);
        if (u != null && "active".equals(u[4]) && u[2].equals(password))
            return new String[]{u[0], u[1], u[3]};
        return null;
    }

    public static List<String[]> all(Context c) {
        List<String[]> list = new ArrayList<>();
        String data = c.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(DATA, "");
        if (data.isEmpty()) return list;
        for (String r : data.split("\\n")) {
            String[] a = r.split("\\|", -1);
            if (a.length >= 5) list.add(a);
        }
        return list;
    }

    public static boolean setStatus(Context c, String email, String status) {
        SharedPreferences p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String data = p.getString(DATA, "");
        StringBuilder out = new StringBuilder();
        boolean changed = false;
        for (String r : data.split("\\n")) {
            String[] a = r.split("\\|", -1);
            if (a.length >= 5 && a[1].equalsIgnoreCase(email)) {
                a[4] = status;
                r = join(a);
                changed = true;
            }
            if (out.length() > 0) out.append('\n');
            out.append(r);
        }
        if (changed) p.edit().putString(DATA, out.toString()).apply();
        return changed;
    }

    private static String[] find(Context c, String email) {
        for (String[] a : all(c))
            if (a[1].equalsIgnoreCase(email)) return a;
        return null;
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
