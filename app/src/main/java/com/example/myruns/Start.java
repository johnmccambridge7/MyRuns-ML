package com.example.myruns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class Start extends Fragment {

    Spinner inputType;
    Spinner activityType;
    Button start;

    String selectedAction = "GPS";
    String selectedActivity = "Running";

    private static final String MANUAL_INPUT = "Manual Input";
    private static final String GPS = "GPS";
    private static final String AUTOMATIC = "Automatic";

    private String unit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        start = view.findViewById(R.id.start);
        inputType = view.findViewById(R.id.inputType);
        activityType = view.findViewById(R.id.activityType);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transition();
            }
        });

        inputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    selectedAction = item.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });
        activityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    selectedActivity = item.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        return view;
    }

    private void transition() {
        Intent i;
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        this.unit = sharedPref.getString("unit", "metric");

        switch(this.selectedAction) {
            case MANUAL_INPUT:
                i = new Intent(getActivity(), ManualEntryActivity.class);
                i.putExtra("activityType", this.selectedActivity);
                i.putExtra("units", this.unit);
                startActivity(i);
                break;
            case GPS:
                i = new Intent(getActivity(), GPSActivity.class);
                i.putExtra("activityType", this.selectedActivity);
                i.putExtra("units", this.unit);
                i.putExtra("startService", true);
                startActivity(i);
                break;
            case AUTOMATIC:
                i = new Intent(getActivity(), GPSActivity.class);
                i.putExtra("activityType", "Unknown");
                i.putExtra("units", this.unit);
                i.putExtra("startService", true);
                startActivity(i);
                break;
        }
    }
}