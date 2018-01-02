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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.ockl.batteryvoltage.utils.Preferences;
import com.github.ockl.batteryvoltage.R;
import com.github.ockl.batteryvoltage.service.BatteryService;

/**
 * Created by greg on 12/28/17.
 */

public class MainFragment extends Fragment {

    private Button startStopButton;
    private Button showInfoButton;
    private Button showGraphButton;

    private Preferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = Preferences.getInstance(getContext().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        startStopButton = (Button) view.findViewById(R.id.button_start_stop);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.isServiceEnabled()) {
                    preferences.setServiceEnabled(false);
                    BatteryService.stopService(getContext().getApplicationContext());
                } else {
                    preferences.setServiceEnabled(true);
                    BatteryService.startService(getContext().getApplicationContext());
                }
            }
        });

        showInfoButton = (Button) view.findViewById(R.id.button_show_info);
        showInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoFragment infoFragment = new InfoFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, infoFragment,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        showGraphButton = (Button) view.findViewById(R.id.button_show_graph);
        showGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartFragment chartFragment = new ChartFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, chartFragment,null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private BroadcastReceiver statusUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateServiceButton();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(statusUpdateReceiver,
                        new IntentFilter(BatteryService.SERVICE_STATUS_UPDATE_NOTIFICATION));
        updateServiceButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext())
                .unregisterReceiver(statusUpdateReceiver);
    }

    private void updateServiceButton() {
        if (preferences.isServiceRunning()) {
            startStopButton.setBackgroundColor(Color.RED);
            startStopButton.setText("Stop");
        } else {
            startStopButton.setBackgroundColor(Color.GREEN);
            startStopButton.setText("Start");
        }
    }

}
