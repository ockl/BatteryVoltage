package com.example.ockl.batteryvoltage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by greg on 12/30/17.
 */

public class UpdateableRecyclerViewFragment extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<String> itemNames = new ArrayList<>();
    private ArrayList<String> itemValues = new ArrayList<>();
    private HashMap<String, Integer> itemIndex = new HashMap<>();

    public void setItemNames(String[] itemNames) {
        this.itemNames = new ArrayList<String>(Arrays.asList(itemNames));
        this.itemValues.clear();
        this.itemIndex.clear();
        for (int i = 0; i < itemNames.length; i++) {
            itemValues.add("");
            itemIndex.put(this.itemNames.get(i), i);
        }
    }

    public void updateValue(String name, String value) {
        int index = itemIndex.get(name);
        String oldValue = itemValues.get(index);
        if (value.compareTo(oldValue) != 0) {
            itemValues.set(index, value);
            recyclerView.getAdapter().notifyItemChanged(index);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter());
        return view;
    }

    private class InfoHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemValue;
        private View seperator;

        public InfoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.info_item, parent, false));
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemValue = (TextView) itemView.findViewById(R.id.item_value);
            seperator = (View) itemView.findViewById(R.id.seperator);
        }

        public void bind(int position, boolean isEnd) {
            itemName.setText(itemNames.get(position));
            itemValue.setText(itemValues.get(position));
            seperator.setVisibility(isEnd ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new InfoHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((InfoHolder) holder).bind(position, position == getItemCount() - 1);
        }

        @Override
        public int getItemCount() {
            return itemNames.size();
        }
    }

}
