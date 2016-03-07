
package com.getstrength.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDITOR_REQUEST_CODE = 1001;

    CursorAdapter cursorAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        cursorAdapter = new NotesCursorAdapter(getActivity(), null, 0);

        ListView list = (ListView) getActivity().findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditorActivity.class);
                Uri uri = Uri.parse(NotesProvider.CONTENT_URI + "/" + id);
                intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.DATE, noteText);
        Uri noteUri = super.getActivity().getContentResolver().insert(NotesProvider.CONTENT_URI,
                values);
        Log.d("MainActivity", "Inserted note " + noteUri.getLastPathSegment());
        restartLoader();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllNotes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            restartLoader();

                            Toast.makeText(getActivity(),
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void insertSampleData() {
        insertNote("Simple note");
        insertNote("Multi-line\nnote");
        insertNote("Very long note with a lot of text that exceeds the width of the screen");
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), NotesProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    public void openEditorForNewNote(View view) {
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            restartLoader();
        }
    }

}
