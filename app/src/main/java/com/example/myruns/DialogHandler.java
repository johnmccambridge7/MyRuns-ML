package com.example.myruns;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.fragment.app.DialogFragment;

public class DialogHandler extends DialogFragment {
    public static final String DIALOG_KEY = "dialog_key";
    private static final String TAG = "DialogHandler";
    public static final int DIALOG_OPTION = 5;
    public static final int DIALOG_UNIT_PREFERENCE = 6;
    public static final int DIALOG_COMMENT = 7;

    private Map<String, String> config;
    private String key;

    String[] options = {
            "Date",
            "Time",
            "Duration",
            "Distance",
            "Calories",
            "Heart Rate",
            "Comment"
    };

    public DialogHandler(String key, Map<String, String> config) {
        this.key = key;
        this.config = config;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("key", key);

        for(String key : options) {
            bundle.putString(key, this.config.get(key));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = null;
        Bundle b = getArguments();

        setRetainInstance(true);

        if(savedInstanceState != null) {
            this.key = savedInstanceState.getString("key");

            for(String key : options) {
                this.config.put(key, savedInstanceState.getString(key));
            }
        }

        if(b.getInt(DIALOG_KEY) == DIALOG_OPTION) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.number_pad_dialog, null);

            Button close = dialog_view.findViewById(R.id.done);
            final EditText input = dialog_view.findViewById(R.id.numberpad);
            final TextView title = dialog_view.findViewById(R.id.title);

            if(b.getBoolean("textBased")) {
                input.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            title.setText(key);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // save the content inside of the fragment
                    ManualEntryActivity.config.put(key, input.getText().toString());
                    ManualEntryActivity.listAdapter.notifyDataSetChanged();
                    dismiss();
                }
            });

            builder.setView(dialog_view);
            d = builder.create();
            return d;
        } else if(b.getInt(DIALOG_KEY) == DIALOG_UNIT_PREFERENCE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.units_preference, null);

            Button close = dialog_view.findViewById(R.id.done);
            RadioGroup units = dialog_view.findViewById(R.id.units);
            RadioButton metric = dialog_view.findViewById(R.id.metric);
            RadioButton imperial = dialog_view.findViewById(R.id.imperial);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();

            // get the stored unit - default metric
            String storedUnit = sharedPref.getString("unit", "metric");

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // save the content inside of the fragment
                    // config.put(key, input.getText().toString());
                    dismiss();
                }
            });

            metric.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    editor.putString("unit", "metric");
                    editor.commit();

                    History.refreshList("metric");
                    History.listAdapter.notifyDataSetChanged();
                }
            });

            imperial.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    editor.putString("unit", "imperial");
                    editor.commit();

                    History.refreshList("imperial");
                    History.listAdapter.notifyDataSetChanged();
                }
            });

            if(storedUnit.equals("metric")) {
                units.check(R.id.metric);
            } else if (storedUnit.equals("imperial")){
                units.check(R.id.imperial);
            }

            builder.setView(dialog_view);
            d = builder.create();

            return d;
        } else if(b.getInt(DIALOG_KEY) == DIALOG_COMMENT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialog_view = getActivity().getLayoutInflater().inflate(R.layout.comment_preference, null);

            Button save = dialog_view.findViewById(R.id.saveComment);
            final EditText comment = dialog_view.findViewById(R.id.comment);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();

            // get the stored comment - default metric is a blank string
            String storedComment = sharedPref.getString("comment", "");

            comment.setText(storedComment);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // save the content inside of the fragment
                    // config.put(key, input.getText().toString());
                    editor.putString("comment", comment.getText().toString());
                    editor.commit();
                    dismiss();
                }
            });

            builder.setView(dialog_view);
            d = builder.create();

            return d;
        }

        return d;
    }
}
