package com.example.dailynotes.Models;


import static com.example.dailynotes.Data.DailyNotesContracts.*;
import static com.example.dailynotes.Data.DailyNotesContracts.databaseEntry.*;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "page_entry")
public class Page implements Serializable {
    @ColumnInfo(name = TITLE_COL)
    private String title;
    @ColumnInfo(name = DESCRIPTION_COL)
    private String description;
    @ColumnInfo(name = DATE_COL)
    private long date;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PAGE_ID)
    private int id;
    @ColumnInfo(name = COLOR_INDEX)
    private String color;

    public Page(String title, long date, String description, int id, String color) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.id = id;
        this.color = color;
    }

    @Ignore
    public Page() {
    }

    public static Page fromContentValues(ContentValues values) {
        if (values == null) return null;
        final Page page = new Page();
        if (values.containsKey(PAGE_ID)) {
            page.id = values.getAsInteger(PAGE_ID);
        }
        if (values.containsKey(DATE_COL)) {
            page.date = values.getAsLong(DATE_COL);
        }
        if (values.containsKey(TITLE_COL)) {
            page.title = values.getAsString(TITLE_COL);
        }
        if (values.containsKey(COLOR_INDEX)) {
            page.color = values.getAsString(COLOR_INDEX);
        }
        if (values.containsKey(DESCRIPTION_COL)) {
            page.description = values.getAsString(DESCRIPTION_COL);
        }
        return page;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
