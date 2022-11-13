package com.example.dailynotes.Data;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DAILY_NOTES.db";
    private static final int DATABASE_VERSION = 3;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE_QUERY =
                " CREATE TABLE " + DailyNotesContracts.databaseEntry.TABLE_NAME +
                        "(" + DailyNotesContracts.databaseEntry.PAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DailyNotesContracts.databaseEntry.TITLE_COL + " TEXT NOT NULL," +
                        DailyNotesContracts.databaseEntry.DESCRIPTION_COL + " TEXT," +
                        DailyNotesContracts.databaseEntry.COLOR_INDEX + " TEXT NOT NULL, "+

                        DailyNotesContracts.databaseEntry.DATE_COL + " INTEGER NOT NULL);";
        Log.e("STRING : ", CREATE_TABLE_QUERY);
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DailyNotesContracts.databaseEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
