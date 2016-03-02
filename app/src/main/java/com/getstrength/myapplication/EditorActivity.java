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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getstrength.myapplication.model.Exercise;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class EditorActivity extends AppCompatActivity {

    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;
    private LinearLayout mContainerView;
    private TextView setNumber;
    private int num = 1;
    private Realm realm;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Configure Realm for the application
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("exerciseData.realm")
                .build();

        //Make this Realm the default
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();

        /*//create some tasks
        realm.beginTransaction();
        Exercise t = realm.createObject(Exercise.class);
        t.setId(UUID.randomUUID().toString());
        t.setReps(10);
        t.setWeight(100);
        t.setSets(3);
        t.setExerciseName("Bench press");
        realm.commitTransaction();*/

        /*RealmResults<Exercise> exercises = realm.allObjects(Exercise.class);
        for (Exercise exercise : exercises) {
            Log.d("Hello", String.format("ID: %s, Reps: %s, Weight: %s, Sets: %s, Exercise: %s", exercise.getId(), exercise.getReps(), exercise.getWeight(), exercise.getSets(), exercise.getExerciseName()));
        }*/

        editor = (EditText) findViewById(R.id.datePicker);
        mContainerView = (LinearLayout) findViewById(R.id.parentView);
        setNumber = (TextView) findViewById(R.id.textView2);

        Intent intent = getIntent();

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String DateToStr = format.format(curDate);

        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(DateToStr);
        } else {
            action = Intent.ACTION_EDIT;
            noteFilter = DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COLUMNS, noteFilter, null, null);
            cursor.moveToFirst();
            oldText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();
        }

        /*final int abTitleId = getResources().getIdentifier(DateToStr, "id", getPackageName());
        findViewById(abTitleId).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Do something
            }
        });*/


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }



    /*private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,
                noteFilter, null);
        Toast.makeText(this, getString(R.string.note_deleted),
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }*/



    /*private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }*/

    public void addSet(View view) {
        num++;
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout rowView = (LinearLayout) inflater.inflate(R.layout.new_set, null);
        TextView setNumber2 = (TextView) rowView.findViewById(R.id.textView3);
        setNumber2.setText("Set " + num + ":");
        mContainerView.addView(rowView, mContainerView.getChildCount());
    }

    public void removeSet(View view) {
        mContainerView.removeViewAt(num - 1);
        if (num > 1) {
            num--;
        }
    }

    public void onClick1(View view) {
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
    public void onBackPressed() {
        //create some tasks
        realm.beginTransaction();
        Exercise t = realm.createObject(Exercise.class);
        t.setId(UUID.randomUUID().toString());
        t.setReps(10);
        t.setWeight(75);
        t.setSets(2);
        t.setExerciseName("deadlift");
        realm.commitTransaction();

        RealmResults<Exercise> exercises = realm.allObjects(Exercise.class);
        for (Exercise exercise : exercises) {
            Log.d("Hello", String.format("ID: %s, Reps: %s, Weight: %s, Sets: %s, Exercise: %s", exercise.getId(), exercise.getReps(), exercise.getWeight(), exercise.getSets(), exercise.getExerciseName()));
        }
    }


}




