package com.example.dailynotes.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dailynotes.Models.Page;

@Database(entities = {Page.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static final String DB_NAME = "NotesDatabase.db";
    private static volatile DataBase instance;
    static synchronized DataBase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static DataBase create(final Context context) {
        return Room.databaseBuilder(
                context,
                DataBase.class,
                DB_NAME).build();
    }

    public abstract PageDao getPageDao();
}
