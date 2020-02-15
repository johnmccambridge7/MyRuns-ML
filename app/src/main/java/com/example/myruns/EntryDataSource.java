package com.example.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
/*
    public static final String PRIMARY_KEY = "_id";
    public static final String INPUT_TYPE = "input_type";
    public static final String ACTIVITY_TYPE = "activity_type";
    public static final String DATE_TIME = "date_time";
    public static final String DURATION = "duration";
    public static final String DISTANCE = "distance";
    public static final String AVG_PACE = "avg_pace";
    public static final String AVG_SPEED = "avg_speed";
    public static final String CALORIES = "calories";
    public static final String CLIMB = "climb";
    public static final String HEARTRATE = "heartrate";
    public static final String COMMENT = "comment";
    public static final String PRIVACY = "privacy";
    public static final String GPS_DATA = "gps_data";
 */

public class EntryDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper helper;
    private String[] columns = {SQLiteHelper.PRIMARY_KEY,
                                SQLiteHelper.INPUT_TYPE,
                                SQLiteHelper.ACTIVITY_TYPE,
                                SQLiteHelper.DATE_TIME,
                                SQLiteHelper.DURATION,
                                SQLiteHelper.DISTANCE,
                                SQLiteHelper.AVG_PACE,
                                SQLiteHelper.AVG_SPEED,
                                SQLiteHelper.CALORIES,
                                SQLiteHelper.CLIMB,
                                SQLiteHelper.HEARTRATE,
                                SQLiteHelper.COMMENT,
                                SQLiteHelper.PRIVACY,
                                SQLiteHelper.GPS_DATA,
                                SQLiteHelper.UNITS};

    public EntryDataSource(Context c) {
        helper = new SQLiteHelper(c);
    }

    public void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    public void close() {
        helper.close();
    }

    public ExerciseEntry createEntry(ExerciseEntry entry) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.INPUT_TYPE, entry.getInputType());
        values.put(SQLiteHelper.ACTIVITY_TYPE, entry.getActivityType());
        values.put(SQLiteHelper.DATE_TIME, entry.getDateTime());
        values.put(SQLiteHelper.DURATION, entry.getDuration());
        values.put(SQLiteHelper.DISTANCE, entry.getDistance());
        values.put(SQLiteHelper.AVG_PACE, entry.getAvgPace());
        values.put(SQLiteHelper.AVG_SPEED, entry.getAvgSpeed());
        values.put(SQLiteHelper.CALORIES, entry.getCalorie());
        values.put(SQLiteHelper.CLIMB, entry.getClimb());
        values.put(SQLiteHelper.HEARTRATE, entry.getHeartRate());
        values.put(SQLiteHelper.COMMENT, entry.getComment());
        values.put(SQLiteHelper.PRIVACY, 1);
        values.put(SQLiteHelper.GPS_DATA, entry.getGpsData());
        values.put(SQLiteHelper.UNITS, entry.getUnits());

        long recordID = database.insert(SQLiteHelper.ENTRIES_TABLE, null, values);
        Cursor cursor = database.query(SQLiteHelper.ENTRIES_TABLE,
                columns,
                SQLiteHelper.PRIMARY_KEY + " = " + recordID, null, null, null, null);

        cursor.moveToFirst();

        ExerciseEntry e = convertCursorToEntry(cursor);

        return e;
    }

    public void deleteEntry(int id) {
        database.delete(SQLiteHelper.ENTRIES_TABLE, SQLiteHelper.PRIMARY_KEY + " = " + id, null);
    }

    public ExerciseEntry convertCursorToEntry(Cursor c) {
        ExerciseEntry e = new ExerciseEntry();


        e.setId(c.getLong(0));
        e.setInputType(c.getInt(1));
        e.setActivityType(c.getString(2));
        e.setDateTime(c.getString(3));
        e.setDuration(c.getInt(4));
        e.setDistance(c.getDouble(5));
        e.setAvgPace(c.getInt(6));
        e.setAvgSpeed(c.getFloat(7));
        e.setCalorie(c.getInt(8));
        e.setClimb(c.getFloat(9));
        e.setHeartRate(c.getInt(10));
        e.setComment(c.getString(11));
        e.setGpsData(c.getString(13));
        e.setUnits(c.getString(14));

        return e;
    }

    public void deleteAllEntries() {
        database.delete(SQLiteHelper.ENTRIES_TABLE, null, null);
    }

    public void updateUnits(String newUnit) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.UNITS, newUnit);

        database.update(SQLiteHelper.ENTRIES_TABLE, values, null, null);
    }

    public void updateEntryDistance(Long id, double distance) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.DISTANCE, distance);
        database.update(SQLiteHelper.ENTRIES_TABLE, values, SQLiteHelper.PRIMARY_KEY + " = " + String.valueOf(id), null);
    }

    public ArrayList<ExerciseEntry> getAllEntries() {
        Cursor c = database.query(SQLiteHelper.ENTRIES_TABLE, columns, null, null, null, null, null);
        c.moveToFirst();

        ArrayList<ExerciseEntry> records = new ArrayList<ExerciseEntry>();

        while(!c.isAfterLast()) {
            ExerciseEntry entry = convertCursorToEntry(c);
            records.add(entry);
            c.moveToNext();
        }

        c.close();

        return records;
    }
}
