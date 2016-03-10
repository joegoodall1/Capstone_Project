package com.getstrength.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getstrength.myapplication.model.Exercise;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditorActivity extends AppCompatActivity {

    Date curDate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    String dateToStr = format.format(curDate);
    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;
    private LinearLayout mContainerView;
    TextView setNumber;
    private int num = 1;
    private Date date;
    String spinnerString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);

        ExerciseStore.initialise(this);


        editor = (EditText) findViewById(R.id.datePicker);
        mContainerView = (LinearLayout) findViewById(R.id.parentView);


        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(ExerciseProvider.URL);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(dateToStr);
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.EXERCISE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.DATE));
            editor.setText(oldText);
            editor.requestFocus();
        }


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.exercise_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerString = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public void addSet(View view) {
        num++;
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.new_set, null);
        TextView setNumber2 = (TextView) rowView.findViewById(R.id.textView3);
        setNumber2.setText("Set " + num + ":");
        mContainerView.addView(rowView, mContainerView.getChildCount());
    }

    public void removeSet(View view) {
        if (num > 1) {
            mContainerView.removeViewAt(num - 1);
            num--;
        }
    }

    public void onSelectDate(View view) {
        Calendar now = Calendar.getInstance();
        final DatePickerDialog d = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
                        Calendar checkedCalendar = Calendar.getInstance();
                        checkedCalendar.set(year, monthOfYear, dayOfMonth);
                        date = checkedCalendar.getTime();
                        action = Intent.ACTION_INSERT;
                        setTitle(DateFormatter.convertDateToString(date));
                        dateToStr = DateFormatter.convertDateToString(date);
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        d.setMaxDate(now);
        d.show(getFragmentManager(), action);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.content_save:
                contentSave();
                break;
        }

        return true;
    }

    private void contentSave() {

        Toast.makeText(this, getString(R.string.exercise_saved),
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        testStoreExercises();
        testGetExerciseDates();
        testGetExercises();
        finish();
    }

    private void testStoreExercises() {

        List<Exercise> exercises = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            View child = mContainerView.getChildAt(i);

            if (!(child instanceof ViewGroup)) {
                continue;
            }

            ViewGroup container = (ViewGroup) child;

            container.getChildAt(i);

            if (i < 1) {

                EditText weight1 = (EditText) findViewById(R.id.wt1);
                EditText reps1 = (EditText) findViewById(R.id.reps1);

                //create some tasks

                String setRepsValue = reps1.getText().toString();
                int setRepsNum = Integer.parseInt(setRepsValue);

                String setWtValue = weight1.getText().toString();
                int setWtNum = Integer.parseInt(setWtValue);


                exercises.add(new Exercise(i + 1, setRepsNum, setWtNum, dateToStr, spinnerString));
            } else {
                EditText weight1 = (EditText) mContainerView.findViewById(R.id.editText1);
                EditText reps1 = (EditText) mContainerView.findViewById(R.id.editText2);

                //create some tasks

                String setRepsValue = reps1.getText().toString();
                int setRepsNum = Integer.parseInt(setRepsValue);

                String setWtValue = weight1.getText().toString();
                int setWtNum = Integer.parseInt(setWtValue);


                exercises.add(new Exercise(i + 1, setRepsNum, setWtNum, dateToStr, spinnerString));
            }


        }

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

        List<Exercise> exercises = ExerciseStore.getInstance().getExercises("10/03/2016");

        for (Exercise exercise : exercises) {
            Log.i("testGetExercises", exercise.toString());
        }
    }
}




