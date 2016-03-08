package com.getstrength.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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
    private Date date;
    private String spinnerString;
    private EditText mWeight1;
    private EditText mReps1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editor);


        editor = (EditText) findViewById(R.id.datePicker);
        mContainerView = (LinearLayout) findViewById(R.id.parentView);
        setNumber = (TextView) findViewById(R.id.textView2);
        mWeight1 = (EditText) findViewById(R.id.wt1);
        mReps1 = (EditText) findViewById(R.id.reps1);


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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.content_save:
                deleteNote();
                break;
        }

        return true;
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.DATE, noteText);
        getContentResolver().insert(ExerciseProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    private void deleteNote() {
        getContentResolver().delete(ExerciseProvider.CONTENT_URI,
                noteFilter, null);
        Toast.makeText(this, getString(R.string.exercise_saved),
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.DATE, noteText);
        getContentResolver().update(ExerciseProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updated), Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {

        //create some tasks
        String setWtValue = mWeight1.getText().toString();
        String setRepsValue = mReps1.getText().toString();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (dateToStr.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    insertNote(dateToStr);
                }
                break;
            case Intent.ACTION_EDIT:
                if (dateToStr.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(dateToStr)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(dateToStr);
                }

        }
        finish();

    }
}




