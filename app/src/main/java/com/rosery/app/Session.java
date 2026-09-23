package com.rosery.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static final String PREF = "rosery_session";
    private SharedPreferences p;

    public Session(Context c) {
        p = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void login(String name, String email, String role) {
        p.edit().putString("name", name).putString("email", email).putString("role", role).putBoolean("logged", true).apply();
    }

    public boolean isLoggedIn() {
        return p.getBoolean("logged", false);
    }

    public boolean isAdmin() {
        return "admin".equals(p.getString("role", ""));
    }

    public String getEmail() {
        return p.getString("email", "");
    }

    public String getName() {
        return p.getString("name", "");
    }

    public void logout() {
        p.edit().clear().apply();
    }
}
