package com.example.dailynotes.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DailyNotesContracts {


    public DailyNotesContracts() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.dailynotes";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PAGES = "pages";

    public static final class databaseEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PAGES);
        public static final String TABLE_NAME = "PAGES";
        public static final String PAGE_ID = "ID";
        public static final String TITLE_COL = "TITLE";
        public static final String DESCRIPTION_COL = "DESCRITPION";
        public static final String DATE_COL = "DATE";
        public static final String COLOR_INDEX="COLOR";
    }
}
