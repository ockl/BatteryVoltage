package com.github.ockl.batteryvoltage.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.ockl.batteryvoltage.service.BatteryService;
import com.github.ockl.batteryvoltage.utils.Preferences;

/**
 * Created by greg on 10/30/17.
 */

public class StartupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Preferences preferences = Preferences.getInstance(context.getApplicationContext());
        if (preferences.isServiceEnabled()) {
            BatteryService.startService(context.getApplicationContext());
        }
    }

}
