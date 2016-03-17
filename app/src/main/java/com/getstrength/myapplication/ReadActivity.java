package com.getstrength.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.getstrength.myapplication.model.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends AppCompatActivity {

    private LinearLayout mContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read);
        ExerciseStore.initialise(this);

        mContainerView = (LinearLayout) findViewById(R.id.parentView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("date_string");
            initialise(value);
        }
    }


    private void initialise(String date) {

        setTitle(date);
        List<Exercise> exercises = ExerciseStore.getInstance().getExercises(date);

        List<Pair<String, List<Exercise>>> exerciseGroups = sortExercises(exercises);

        for (Pair<String, List<Exercise>> exerciseGroup : exerciseGroups) {
            addSetView(exerciseGroup.first, exerciseGroup.second);
        }
    }

    private List<Pair<String, List<Exercise>>> sortExercises(List<Exercise> exercises) {

        List<String> exerciseNames = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (!exerciseNames.contains(exercise.getExerciseName())) {
                exerciseNames.add(exercise.getExerciseName());
            }
        }

        List<Pair<String, List<Exercise>>> exerciseGroups = new ArrayList<>();

        for (String exerciseName : exerciseNames) {

            List<Exercise> exercisesInGroup = new ArrayList<>();

            for (Exercise exercise : exercises) {
                if (exercise.getExerciseName().equals(exerciseName)) {
                    exercisesInGroup.add(exercise);
                }
            }

            exerciseGroups.add(new Pair<>(exerciseName, exercisesInGroup));
        }

        return exerciseGroups;
    }

    private void addSetView(String exerciseName, List<Exercise> exercises) {

        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout exerciseGroupView = (LinearLayout) inflater.inflate(R.layout.read_set, null);
        TextView exerciseGroupName = (TextView) exerciseGroupView.findViewById(R.id.ex_name);
        exerciseGroupName.setText(exerciseName);

        Space space = (Space) inflater.inflate(R.layout.spacer, null);

        for (int i = 0; i < exercises.size(); ++i) {

            Exercise exercise = exercises.get(i);

            final LinearLayout exerciseRecord = (LinearLayout) inflater.inflate(R.layout.exercise_record, null);

            TextView setNumber = (TextView) exerciseRecord.findViewById(R.id.set_number);
            setNumber.setText("Set " + i + 1 + ":");

            TextView weight = (TextView) exerciseRecord.findViewById(R.id.weight);
            weight.setText("" + exercise.getWeight());

            TextView reps = (TextView) exerciseRecord.findViewById(R.id.reps);
            reps.setText("" + exercise.getReps());

            exerciseGroupView.addView(exerciseRecord);
        }

        mContainerView.addView(exerciseGroupView);
        mContainerView.addView(space);
    }
}




