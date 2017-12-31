package com.example.ockl.batteryvoltage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

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
