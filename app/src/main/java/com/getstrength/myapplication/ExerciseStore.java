package com.getstrength.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExerciseStore {

    // Mock class - replace with real Exercise class in app.
    public static class Exercise {

        public int id;
        public int sets;
        public int reps;
        public int weight;
        public String date;
        public String exercise;

        public Exercise(int id, int sets, int reps, int weight, String date, String exercise) {
            this(sets, reps, weight, date, exercise);
            this.id = id;
        }

        public Exercise(int sets, int reps, int weight, String date, String exercise) {
            this.sets = sets;
            this.reps = reps;
            this.weight = weight;
            this.date = date;
            this.exercise = exercise;
        }

        @Override
        public String toString() {
            return String.format(Locale.getDefault(), "_id=%d, Sets=%d, Reps=%d, Weight=%d, Date=%s, Exercise=%s", id, sets, reps, weight, date, exercise);
        }
    }

    private static ExerciseStore mInstance;

    private Context mContext;

    private ExerciseStore(Context context) {
        mContext = context;
    }

    public static void initialise(Context context) {
        mInstance = new ExerciseStore(context);
    }

    public static ExerciseStore getInstance() {
        return mInstance;
    }

    public boolean storeExercises(List<Exercise> exercises) {

        try {
            ContentResolver contentResolver = mContext.getContentResolver();

            for (Exercise exercise : exercises) {

                ContentValues values = new ContentValues();
                values.put(DBOpenHelper.DATE, exercise.date);
                values.put(DBOpenHelper.EXERCISE, exercise.exercise);
                values.put(DBOpenHelper.SETS, exercise.sets);
                values.put(DBOpenHelper.REPS, exercise.reps);
                values.put(DBOpenHelper.WEIGHT, exercise.weight);

                contentResolver.insert(ExerciseProvider.CONTENT_URI, values);
            }

            return true;
        } catch (Exception exception) {
            Log.e(ExerciseStore.class.getName(), "Error storing exercise.", exception);
            return false;
        }
    }

    public List<String> getExerciseDates() {

        Cursor cursor = null;

        try {
            ContentResolver contentResolver = mContext.getContentResolver();

            String[] projection = {"DISTINCT " + DBOpenHelper.DATE};
            cursor = contentResolver.query(ExerciseProvider.CONTENT_URI, projection, null, null, DBOpenHelper.DATE + " ASC");
            if (cursor == null) {
                return null;
            }

            List<String> dates = new ArrayList<>();

            while (cursor.moveToNext()) {
                dates.add(cursor.getString(0));
            }

            return dates;
        } catch (Exception exception) {
            Log.e(ExerciseStore.class.getName(), "Error reading dates.", exception);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Exercise> getExercises(String date) {

        Cursor cursor = null;

        try {
            ContentResolver contentResolver = mContext.getContentResolver();

            String where = DBOpenHelper.DATE + "='" + date + "'";
            cursor = contentResolver.query(ExerciseProvider.CONTENT_URI, DBOpenHelper.ALL_COLUMNS, where, null, null);
            if (cursor == null) {
                return null;
            }

            List<Exercise> exercises = new ArrayList<>();

            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String exerciseDate = cursor.getString(1);
                String exerciseName = cursor.getString(2);
                int sets = cursor.getInt(3);
                int reps = cursor.getInt(4);
                int weight = cursor.getInt(5);

                exercises.add(new Exercise(id, sets, reps, weight, exerciseDate, exerciseName));
            }

            return exercises;
        } catch (Exception exception) {
            Log.e(ExerciseStore.class.getName(), "Error reading exercises.", exception);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
