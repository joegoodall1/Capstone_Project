package com.getstrength.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    // Constants for db name and version
    private static final String DATABASE_NAME = "exercise.db";
    private static final int DATABASE_VERSION = 1;

    // Constants for identifying table and columns
    public static final String TABLE_EXERCISE = "exercise";
    public static final String EXERCISE_ID = "_id";
    public static final String DATE = "date";
    public static final String EXERCISE = "exerciseName";
    public static final String SETS = "sets";
    public static final String REPS = "reps";
    public static final String WEIGHT = "weight";

    public static final String[] ALL_COLUMNS =
            {EXERCISE_ID, DATE, EXERCISE, SETS, REPS, WEIGHT};

    // SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EXERCISE + " (" +
                    EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATE + " TEXT," +
                    EXERCISE + " TEXT," +
                    SETS + " INT," +
                    REPS + " INT," +
                    WEIGHT + " INT" +
                    ");";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No plans for updating the application yet.
    }


}

