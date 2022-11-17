package com.example.dailynotes.Data;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dailynotes.Models.Page;

@Dao
public interface PageDao {
    @Query("SELECT * FROM PAGE_ENTRY ORDER BY " +
            DailyNotesContracts.databaseEntry.DATE_COL + " DESC")
    Cursor loadAllPages();

    @Query("SELECT * FROM PAGE_ENTRY WHERE " +
            DailyNotesContracts.databaseEntry.PAGE_ID + " = :id ")
    Cursor loadPage(final int id);

    @Insert
    long insert(Page page);

    @Query("DELETE FROM PAGE_ENTRY WHERE " +
            DailyNotesContracts.databaseEntry.PAGE_ID + " = :id ")
    int deletePage(int id);
    @Update
    int update(Page page);
}
