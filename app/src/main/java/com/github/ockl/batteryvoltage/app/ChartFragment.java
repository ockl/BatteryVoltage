package com.github.ockl.batteryvoltage.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.ockl.batteryvoltage.R;
import com.github.ockl.batteryvoltage.db.Db;
import com.github.ockl.batteryvoltage.db.DbSchema;
import com.github.ockl.batteryvoltage.service.BatteryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 12/31/17.
 */

public class ChartFragment extends Fragment {

    private Db db;
    private LineChart chart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Db.getInstance(getContext().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);
        chart = (LineChart) view.findViewById(R.id.chart);

        updateChart(db.getAll());

        return view;
    }

    private void updateChart(List<DbSchema.Data> update) {
        if (update.size() == 0) {
            return;
        }

        Log.d("bla", "adding " + update.size() + " data points");

        List<Entry> levelEntries = new ArrayList<>();
        List<Entry> voltageEntries = new ArrayList<>();
        List<Entry> capacityEntries = new ArrayList<>();

        float counter = 1.0f;
        for (DbSchema.Data data : update) {
            levelEntries.add(new Entry(counter, (float) data.level));
            voltageEntries.add(new Entry(counter, (float) data.voltage));
            capacityEntries.add(new Entry(counter, (float) data.capacity));
            counter += 1.0f;
        }

        LineDataSet levelDataSet = new LineDataSet(levelEntries, "Level");
        LineDataSet voltageDataSet = new LineDataSet(voltageEntries, "Voltage");
        LineDataSet capacityDataSet = new LineDataSet(capacityEntries, "Capacity");

        levelDataSet.setColor(Color.GREEN);
        levelDataSet.setCircleColor(Color.GREEN);
        voltageDataSet.setColor(Color.BLUE);
        voltageDataSet.setCircleColor(Color.BLUE);
        capacityDataSet.setColor(Color.RED);
        capacityDataSet.setCircleColor(Color.RED);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(levelDataSet);
        dataSets.add(voltageDataSet);
        dataSets.add(capacityDataSet);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.invalidate();
    }

}
