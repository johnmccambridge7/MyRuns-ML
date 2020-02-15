package com.example.myruns;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class HistoryListAdapter extends ArrayAdapter<ExerciseEntry> {
    private final Activity context;
    private final ArrayList<ExerciseEntry> options;

    public HistoryListAdapter(Activity context, ArrayList<ExerciseEntry> options) {
        super(context, R.layout.manual_entry_choice, options);
        this.context = context;
        this.options = options;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.history_entry_choice, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.historyTitle);
        TextView subTitle = rowView.findViewById(R.id.historySubtitle);

        ExerciseEntry entry = options.get(position);

        String unit = " kilometers ";

        if(entry.getUnits().equals("imperial")) {
            // convert value too
            unit = " miles ";
        }

        String entryType = "Manual Entry";

        if(entry.getInputType() == 2) {
            entryType = "GPS";
        }

        txtTitle.setText(entryType + ": " + entry.getActivityType() + ", " + entry.getDateTime());
        subTitle.setText(String.valueOf(entry.getDistance()) + unit + String.valueOf(entry.getDuration()) + " mins and 0 secs");

        return rowView;
    }
}
