package com.example.myruns;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends PreferenceFragmentCompat {
    Map<String, String> config;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        this.config = new HashMap<String, String>();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // get the stored unit - default metric
        String storedUnit = sharedPref.getString("unit", "metric");

        this.config.put("unit", storedUnit);

        Preference unit = (Preference) findPreference("unit");
        unit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here

                DialogHandler dialog = new DialogHandler(null, config);
                Bundle b = new Bundle();
                b.putInt(DialogHandler.DIALOG_KEY, DialogHandler.DIALOG_UNIT_PREFERENCE);
                dialog.setArguments(b);
                dialog.show(getFragmentManager(), "tag");

                return true;
            }
        });

        Preference comment = (Preference) findPreference("comment");

        comment.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here

                DialogHandler dialog = new DialogHandler(null, config);
                Bundle b = new Bundle();
                b.putInt(DialogHandler.DIALOG_KEY, DialogHandler.DIALOG_COMMENT);
                dialog.setArguments(b);
                dialog.show(getFragmentManager(), "tag");

                return true;
            }
        });

    }
}