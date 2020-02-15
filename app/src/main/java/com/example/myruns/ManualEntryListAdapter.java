package com.example.myruns;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Map;

public class ManualEntryListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] options;
    private final Map<String, String> config;

    public ManualEntryListAdapter(Activity context,
                                  String[] options, Map<String, String> config) {
        super(context, R.layout.manual_entry_choice, options);
        this.context = context;
        this.options = options;
        this.config = config;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.manual_entry_choice, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView subTitle = rowView.findViewById(R.id.subtxt);

        txtTitle.setText(options[position]);

        String suffix = "";

        switch(options[position]) {
            case "Duration":
                suffix = "mins";
                break;
            case "Calories":
                suffix = "cals";
                break;
            case "Distance":
                suffix = "units";
                break;
            case "Heart Rate":
                suffix = "bpm";
                break;
        }

        if(!config.get(options[position]).isEmpty()) {
            subTitle.setText(String.format("%s %s", config.get(options[position]), suffix));
        } else {
            subTitle.setText("");
        }

        return rowView;
    }
}
