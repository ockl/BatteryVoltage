package com.github.ockl.batteryvoltage.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.github.ockl.batteryvoltage.db.Db;
import com.github.ockl.batteryvoltage.utils.Preferences;
import com.github.ockl.batteryvoltage.utils.BatteryInfoPublisher;

/**
 * Created by greg on 1/2/18.
 */

public class BatteryService extends Service {

    public static String SERVICE_STATUS_UPDATE_NOTIFICATION =
            "service_status_update_notification";

    private Preferences preferences;
    private BatteryInfoPublisher batteryInfoPublisher;
    private Db db;

    public static void startService(Context context) {
        context.startService(new Intent(context, BatteryService.class));
    }

    public static void stopService(Context context) {
        context.stopService(new Intent(context, BatteryService.class));
    }

    private BatteryInfoPublisher.Receiver batteryInfoReceiver = new BatteryInfoPublisher.Receiver() {
        @Override
        public void onUpdate() {
            db.addData(batteryInfoPublisher.getBatteryLevel(),
                    batteryInfoPublisher.getBatteryVoltage(),
                    batteryInfoPublisher.getBatteryConsumedCapacity());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = Preferences.getInstance(getApplicationContext());
        preferences.setServiceRunning(true);
        notifyActivityStatus();

        batteryInfoPublisher = BatteryInfoPublisher.getInstance(
                getApplicationContext());
        batteryInfoPublisher.subscribe(batteryInfoReceiver);

        db = Db.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        batteryInfoPublisher.unsubscribe(batteryInfoReceiver);

        preferences.setServiceRunning(false);
        notifyActivityStatus();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyActivityStatus() {
        Intent intent = new Intent(SERVICE_STATUS_UPDATE_NOTIFICATION);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }

}
