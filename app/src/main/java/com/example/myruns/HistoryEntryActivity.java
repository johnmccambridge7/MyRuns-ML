package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class HistoryEntryActivity extends AppCompatActivity {

    String entryID;
    Integer position;

    EditText activityType;
    EditText date;
    EditText duration;
    EditText distance;
    EditText calories;
    EditText heartRate;
    EditText inputType;

    EntryDataSource database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_entry);

        Intent i = getIntent();

        this.activityType = findViewById(R.id.historyActivityType);
        this.date = findViewById(R.id.historyDate);
        this.duration = findViewById(R.id.historyDuration);
        this.distance = findViewById(R.id.historyDistance);
        this.calories = findViewById(R.id.calories);
        this.heartRate = findViewById(R.id.heartRate);
        this.inputType = findViewById(R.id.inputType);

        /*intent.putExtra("inputType", "Manual Entry");
        intent.putExtra("activityType", entry.getActivityType());
        intent.putExtra("date", entry.getDateTime());
        intent.putExtra("duration", String.valueOf(entry.getDuration()) + " mins and 0 secs");
        intent.putExtra("distance", String.valueOf(entry.getDistance()) + " Miles");
        intent.putExtra("comment", entry.getComment());
        intent.putExtra("calories", String.valueOf(entry.getCalorie()) + "cals");
        intent.putExtra("heartRate", String.valueOf(entry.getHeartRate()) + " bpm");*/

        this.activityType.setText(i.getStringExtra("activityType"));
        this.date.setText(i.getStringExtra("date"));
        this.duration.setText(i.getStringExtra("duration"));
        this.distance.setText(i.getStringExtra("distance"));
        this.calories.setText(i.getStringExtra("calories"));
        this.heartRate.setText(i.getStringExtra("heartRate"));
        this.inputType.setText(i.getStringExtra("inputType"));
        this.entryID = i.getStringExtra("entryID");
        this.position = i.getIntExtra("position", 0);

        this.database = new EntryDataSource(this);
        this.database.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void deleteEntry(MenuItem item) {
        // new thread to delete entry - DONE
        // close activity - DONE
        // refresh adapter - DONE
        // need to handle metric change
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.deleteEntry(Integer.valueOf(entryID));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Entry Deleted!", Toast.LENGTH_LONG).show();
                        History.removeItem(position);
                        History.listAdapter.notifyDataSetChanged();
                        finish();
                    }
                });
            }
        }).start();

    }
}
