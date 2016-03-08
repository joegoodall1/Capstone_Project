package com.getstrength.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class ExerciseProvider extends ContentProvider {

    private static final String AUTHORITY = "com.getstrength.provider.exercises";
    static final String URL = "content://" + AUTHORITY + "/exercises";
    static final Uri CONTENT_URI = Uri.parse(URL);

    private SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        mDatabase = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(@Nullable Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mDatabase.query(DBOpenHelper.TABLE_EXERCISE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(@Nullable Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@Nullable Uri uri, ContentValues values) {
        long id = mDatabase.insert(DBOpenHelper.TABLE_EXERCISE, null, values);
        return Uri.parse("" + "/" + id);
    }

    @Override
    public int delete(@Nullable Uri uri, String selection, String[] selectionArgs) {
        return mDatabase.delete(DBOpenHelper.TABLE_EXERCISE, selection, selectionArgs);
    }

    @Override
    public int update(@Nullable Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mDatabase.update(DBOpenHelper.TABLE_EXERCISE, values, selection, selectionArgs);
    }
}
