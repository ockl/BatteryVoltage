package com.github.ockl.batteryvoltage.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.ockl.batteryvoltage.utils.UpdateableRecyclerViewFragment;
import com.github.ockl.batteryvoltage.utils.BatteryInfoPublisher;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by greg on 12/28/17.
 */

public class InfoFragment extends UpdateableRecyclerViewFragment {

    private final String NameTime = "Last update";
    private final String NameStatus = "Status";
    private final String NameHealth = "Health";
    private final String NameLevel = "Level";
    private final String NamePlugged = "Plugged";
    private final String NameVoltage = "Voltage";
    private final String NameTemperature = "Temperature";
    private final String NameConsumedCapacity = "Consumed capacity";
    private final String NameCurrentAverage = "Current (average)";

    private final String[] Names = { NameTime, NameHealth, NameStatus,
            NameLevel, NamePlugged, NameVoltage, NameTemperature,
            NameConsumedCapacity, NameCurrentAverage };

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private BatteryInfoPublisher.Receiver batteryInfoReceiver = new BatteryInfoPublisher.Receiver() {
        @Override
        public void onUpdate() {
            updateValue(NameTime, dateFormat.format(
                    Calendar.getInstance().getTime()));
            updateValue(NameStatus, batteryInfoPublisher.getBatteryStatus());
            updateValue(NameHealth, batteryInfoPublisher.getBatteryHealth());
            updateValue(NameLevel, Integer.toString(
                    batteryInfoPublisher.getBatteryLevel()) + "%");
            updateValue(NamePlugged, batteryInfoPublisher.getBatteryPlugged());
            updateValue(NameVoltage, Double.toString(
                    batteryInfoPublisher.getBatteryVoltage()) + " V");
            updateValue(NameTemperature, Double.toString(
                    batteryInfoPublisher.getBatteryTemperature()) + " °C");
            updateValue(NameConsumedCapacity, Double.toString(
                    batteryInfoPublisher.getBatteryConsumedCapacity()) + " mAh");
            updateValue(NameCurrentAverage, Double.toString(
                    batteryInfoPublisher.getBatteryCurrentAverage()) + " mA");
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setItemNames(Names);
    }

    BatteryInfoPublisher batteryInfoPublisher;

    @Override
    public void onResume() {
        super.onResume();
        batteryInfoPublisher = BatteryInfoPublisher.getInstance(
                getContext().getApplicationContext());
        batteryInfoPublisher.subscribe(batteryInfoReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        batteryInfoPublisher.unsubscribe(batteryInfoReceiver);
    }

}
