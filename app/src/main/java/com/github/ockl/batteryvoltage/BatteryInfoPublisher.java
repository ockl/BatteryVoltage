package com.github.ockl.batteryvoltage;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Handler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by greg on 12/30/17.
 */

public class BatteryInfoPublisher {

    interface Receiver {
        void onUpdate();
    }

    private class ReceiverInternal {
        private final Receiver receiver;
        private final Handler handler;

        private Runnable asyncOnUpdate = new Runnable() {
            @Override
            public void run() {
                receiver.onUpdate();
            }
        };

        public ReceiverInternal(Receiver receiver) {
            this.receiver = receiver;
            handler = new Handler();
        }

        public void publish() {
            // no specific reason for doing this async, just for fun
            handler.post(asyncOnUpdate);
        }

        public void destroy() {
            handler.removeCallbacks(asyncOnUpdate);
        }
    }

    private HashMap<Receiver, ReceiverInternal> receivers = new HashMap<>();

    // true if subscriptions exist and our internal subscription to battery
    // changed events is active, otherwise false.
    private boolean isActive = false;
    // true if, after subscribing the battery changed events, we have
    // received at least one set of data, false otherwise.
    private boolean hasUpdates = false;

    // TODO: this will effectively subscribe all fields, could be done more selectively
    public void subscribe(final Receiver receiver) {
        ReceiverInternal receiverInternal = new ReceiverInternal(receiver);
        receivers.put(receiver, receiverInternal);
        if (!isActive) {
            activate();
        } else if (hasUpdates) {
            receiverInternal.publish();
        }
    }

    public void unsubscribe(Receiver receiver) {
        ReceiverInternal receiverInternal = receivers.remove(receiver);
        receiverInternal.destroy();
        if (receivers.isEmpty() && isActive) {
            deactivate();
        }
    }

    private void publish() {
        for (Map.Entry<Receiver, ReceiverInternal> receiver : receivers.entrySet()) {
            receiver.getValue().publish();
        }
    }

    private final Context context;
    private final Handler handler;
    private final BatteryManager batteryManager;

    private static BatteryInfoPublisher instance;

    public static BatteryInfoPublisher getInstance(Context context) {
        if (instance == null) {
            instance = new BatteryInfoPublisher(context);
        }
        return instance;
    }

    private BatteryInfoPublisher(Context context) {
        this.context = context;
        this.handler = new Handler();
        batteryManager = (BatteryManager)
                context.getSystemService(Context.BATTERY_SERVICE);
    }

    private int batteryStatus;
    private int batteryHealth;
    private int batteryLevel;
    private int batteryPlugged;
    private int batteryVoltage;
    private int batteryTemperature;
    private int batteryConsumedCapacity;
    private int batteryCurrentAverage;

    public String getBatteryStatus() {
        switch (batteryStatus) {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                return "Unknown";
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "Charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return "Discharging";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "Not charging";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "Full";
            default:
                return "Unknown";
        }
    }

    public String getBatteryHealth() {
        switch (batteryHealth) {
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                return "Unknown";
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return "Good";
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                return "Overheat";
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return "Dead";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return "Over voltage";
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return "Unspecified failure";
            case BatteryManager.BATTERY_HEALTH_COLD:
                return "Cold";
            default:
                return "Unknown";
        }
    }

    // in %
    public int getBatteryLevel() {
        return batteryLevel;
    }

    public String getBatteryPlugged() {
        switch (batteryPlugged) {
            case 0:
                return "Unplugged";
            case BatteryManager.BATTERY_PLUGGED_AC:
                return "AC";
            case BatteryManager.BATTERY_PLUGGED_USB:
                return "USB";
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return "Wireless";
            default:
                return "Unknown";
        }
    }

    // in Volt
    public double getBatteryVoltage() {
        return (double) batteryVoltage / 1000.0;
    }

    // in °C
    public double getBatteryTemperature() {
        return (double) batteryTemperature / 10.0;
    }

    // in mAh
    public double getBatteryConsumedCapacity() {
        return (double) batteryConsumedCapacity / 1000.0;
    }

    // in mA
    public double getBatteryCurrentAverage() {
        return (double) batteryCurrentAverage / 1000.0;
    }

    private BroadcastReceiver batteryIntentReader = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            batteryHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            batteryPlugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            batteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);

            // use the opportunity to update the battery properties as well
            readBatteryProperties();
            resetTimer(true);

            hasUpdates = true;
            publish();
        }
    };

    private void readBatteryProperties() {
        batteryConsumedCapacity = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        batteryCurrentAverage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
    }

    private Runnable batteryPropertyReader = new Runnable() {
        @Override
        public void run() {
            readBatteryProperties();
            publish();
        }
    };

    private void resetTimer(boolean restart) {
        handler.removeCallbacks(batteryPropertyReader);
        if (restart) {
            handler.postDelayed(batteryPropertyReader, 60 * 1000);
        }
    }

    private void activate() {
        context.registerReceiver(batteryIntentReader,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        isActive = true;
    }

    private void deactivate() {
        context.unregisterReceiver(batteryIntentReader);
        resetTimer(false);
        isActive = false;
        hasUpdates = false;
    }

}
