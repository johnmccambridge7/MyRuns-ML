package com.example.myruns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String ENTRIES_TABLE = "entries";

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
    public static final String UNITS = "units";

    private static final String DATABASE_NAME = "entries.db";
    private static final int DATABASE_VERSION = 10;

    private static final String DB_CREATION =
                     "CREATE TABLE IF NOT EXISTS " + ENTRIES_TABLE + " (\n" +
            "        " + PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            "        " + INPUT_TYPE + " INTEGER NOT NULL, \n" +
            "        " + ACTIVITY_TYPE + " TEXT NOT NULL, \n" +
            "        " + DATE_TIME + " DATETIME NOT NULL, \n" +
            "        " + DURATION + " INTEGER NOT NULL, \n" +
            "        " + DISTANCE + " REAL, \n" +
            "        " + AVG_PACE + " REAL, \n" +
            "        " + AVG_SPEED + " REAL,\n" +
            "        " + CALORIES + " INTEGER, \n" +
            "        " + CLIMB + " REAL, \n" +
            "        " + HEARTRATE + " INTEGER, \n" +
            "        " + COMMENT + " TEXT, \n" +
            "        " + PRIVACY + " INTEGER,\n" +
            "        " + GPS_DATA + " TEXT,\n " +
            "        " + UNITS + " TEXT);";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DB_CREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ENTRIES_TABLE);
        onCreate(db);
    }

}