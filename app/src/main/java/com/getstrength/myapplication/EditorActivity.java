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
import android.widget.AdapterView;
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

    Date curDate = new Date();
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    String dateToStr = format.format(curDate);
    private String action;
    private EditText editor;
    private String noteFilter;
    private String oldText;
    private LinearLayout mContainerView;
    private TextView setNumber;
    private int num = 1;
    private Realm realm;
    private Date date;
    private String spinnerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Configure Realm for the application
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("exerciseData.realm")
               /* .deleteRealmIfMigrationNeeded()*/
                .build();

        //Make this Realm the default
        Realm.setDefaultConfiguration(config);

        realm = Realm.getDefaultInstance();


        editor = (EditText) findViewById(R.id.datePicker);
        mContainerView = (LinearLayout) findViewById(R.id.parentView);
        setNumber = (TextView) findViewById(R.id.textView2);

        Intent intent = getIntent();


        Uri uri = intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(dateToStr);
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
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
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
    public void onBackPressed() {
        //create some tasks
        realm.beginTransaction();
        Exercise t = realm.createObject(Exercise.class);
        t.setId(UUID.randomUUID().toString());
        t.setReps(10);
        t.setWeight(70);
        t.setSets(2);
        t.setDate(dateToStr);
        t.setExerciseName(spinnerString);
        realm.commitTransaction();

        RealmResults<Exercise> exercises = realm.allObjects(Exercise.class);
        for (Exercise exercise : exercises) {
            Log.d("Hello", String.format("ID: %s, Date: %s, Reps: %s, Weight: %s, Sets: %s, Exercise: %s", exercise.getId(), exercise.getDate(), exercise.getReps(), exercise.getWeight(), exercise.getSets(), exercise.getExerciseName()));
        }
    }


}




