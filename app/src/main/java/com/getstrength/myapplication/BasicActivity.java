package com.getstrength.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BasicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);

        ExerciseStore.initialise(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onClickAddName(View view) {
        testStoreExercises();
        testGetExerciseDates();
        testGetExercises();
    }

    private void testStoreExercises() {

        List<ExerciseStore.Exercise> exercises = new ArrayList<>();

        exercises.add(new ExerciseStore.Exercise(1, 2, 3, "26/03/2016", "deadlift"));
        exercises.add(new ExerciseStore.Exercise(2, 3, 4, "26/03/2016", "benchpress"));
        exercises.add(new ExerciseStore.Exercise(5, 6, 7, "26/03/2016", "squat"));
        exercises.add(new ExerciseStore.Exercise(8, 9, 10, "26/03/2016", "burpee"));

        exercises.add(new ExerciseStore.Exercise(1, 2, 3, "27/03/2016", "deadlift"));
        exercises.add(new ExerciseStore.Exercise(2, 3, 4, "27/03/2016", "benchpress"));
        exercises.add(new ExerciseStore.Exercise(5, 6, 7, "27/03/2016", "squat"));
        exercises.add(new ExerciseStore.Exercise(8, 9, 10, "27/03/2016", "burpee"));

        exercises.add(new ExerciseStore.Exercise(1, 2, 3, "28/03/2016", "deadlift"));
        exercises.add(new ExerciseStore.Exercise(2, 3, 4, "28/03/2016", "benchpress"));
        exercises.add(new ExerciseStore.Exercise(5, 6, 7, "28/03/2016", "squat"));
        exercises.add(new ExerciseStore.Exercise(8, 9, 10, "28/03/2016", "burpee"));

        boolean success = ExerciseStore.getInstance().storeExercises(exercises);
        Log.i("testStoreExercises", "success = " + success);
    }

    private void testGetExerciseDates() {

        List<String> dates = ExerciseStore.getInstance().getExerciseDates();

        for (String date : dates) {
            Log.i("testGetExerciseDates", date);
        }
    }

    private void testGetExercises() {

        List<ExerciseStore.Exercise> exercises = ExerciseStore.getInstance().getExercises("26/03/2016");

        for (ExerciseStore.Exercise exercise : exercises) {
            Log.i("testGetExercises", exercise.toString());
        }
    }


}
