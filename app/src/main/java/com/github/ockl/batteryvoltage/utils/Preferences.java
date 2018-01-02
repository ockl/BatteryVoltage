package com.github.ockl.batteryvoltage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by greg on 1/2/18.
 */

public class Preferences {

    private final String ServiceEnabled = "service_enabled";
    private final String ServiceRunning = "service_running";

    private static Preferences instance;

    public static Preferences getInstance(Context context) {
        synchronized (Preferences.class) {
            if (instance == null) {
                instance = new Preferences(context);
            }
        }
        return instance;
    }

    private SharedPreferences preferences;

    private Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isServiceEnabled() {
        return preferences.getBoolean(ServiceEnabled, false);
    }

    public void setServiceEnabled(boolean enabled) {
        preferences.edit().putBoolean(ServiceEnabled, enabled).commit();
    }

    public boolean isServiceRunning() {
        return preferences.getBoolean(ServiceRunning, false);
    }

    public void setServiceRunning(boolean running) {
        preferences.edit().putBoolean(ServiceRunning, running).commit();
    }

}
