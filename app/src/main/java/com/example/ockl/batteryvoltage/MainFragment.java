package com.example.ockl.batteryvoltage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by greg on 12/28/17.
 */

public class MainFragment extends Fragment {

    private Button startStopButton;
    private Button showInfoButton;
    private Button showGraphButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        startStopButton = (Button) view.findViewById(R.id.button_start_stop);
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
        return view;
    }

}
