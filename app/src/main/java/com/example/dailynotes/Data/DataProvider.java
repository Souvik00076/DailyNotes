package com.example.dailynotes.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataProvider extends ContentProvider {
    private static final UriMatcher matcher;
    private static final int PAGES_ID=110;
    private static final int PAGE_ID=111;
    private DatabaseHelper helper;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(DailyNotesContracts.CONTENT_AUTHORITY, DailyNotesContracts.PATH_PAGES, PAGES_ID);
        matcher.addURI(DailyNotesContracts.CONTENT_AUTHORITY, DailyNotesContracts.PATH_PAGES + "/#", PAGE_ID);
    }

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArguments, @Nullable String sortOrder) {
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor;
        int id=matcher.match(uri);
        switch (id){
            case PAGE_ID:

                selection = DailyNotesContracts.databaseEntry.PAGE_ID+ "=?";
                selectionArguments = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(DailyNotesContracts.databaseEntry.TABLE_NAME,
                        projection, selection, selectionArguments, null, null, null);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            case PAGES_ID:
                cursor = database.query(DailyNotesContracts.databaseEntry.TABLE_NAME,
                        projection, selection, selectionArguments, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = helper.getWritableDatabase();
        long id = database.insert(DailyNotesContracts.databaseEntry.TABLE_NAME, null, contentValues);
        if (id == -1) {
            Toast.makeText(getContext(), "EROR OCCURED WHILE INSERTING!!!", Toast.LENGTH_SHORT).show();
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database=helper.getWritableDatabase();
        int rowsDeleted=0;
        rowsDeleted=database.delete(DailyNotesContracts.databaseEntry.TABLE_NAME,selection,selectionArgs);
        if(rowsDeleted!=0) getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String title=values.getAsString(DailyNotesContracts.databaseEntry.TITLE_COL);
        selection=DailyNotesContracts.databaseEntry.PAGE_ID+"=?";
        selectionArgs=new String[]{values.getAsString(DailyNotesContracts.databaseEntry.PAGE_ID)};
        if(title==null || title.length()==0)
            throw new IllegalArgumentException("Note Require a title");
        int rowsUpdated = database.update(DailyNotesContracts.databaseEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
