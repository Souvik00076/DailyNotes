package com.example.dailynotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dailynotes.Adapters.ContentAdapter;
import com.example.dailynotes.Models.Note;
import com.example.dailynotes.Preferences.Settings;
import com.example.dailynotes.Data.DailyNotesContracts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        ContentAdapter.onPageClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private FloatingActionButton newNote;
    private ContentAdapter adapter;
    private RecyclerView recyclerView;
    private TextView noTextView;
    private boolean isGrid;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setUpSharedPreferences();
        newNote.setOnClickListener(view -> {
            Intent openNewNote = new Intent(getApplicationContext(), Note.class);
            startActivity(openNewNote);
        });
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    private void setUpSharedPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isGrid = preferences.getBoolean("Grid View", getResources().getBoolean(R.bool.dont_show_grid));
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("Grid View")) {
            isGrid = sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.dont_show_grid));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.
                getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(getApplicationContext(), Settings.class);
                startActivity(settingsIntent);
                return true;
            case R.id.about:
                startActivity(new Intent(getApplicationContext(), AboutPage.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews() {

        newNote = (FloatingActionButton) findViewById(R.id.newNote);
        noTextView = (TextView) findViewById(R.id.no_text_id);
        noTextView.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.page_holder_view);
        adapter = new ContentAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                DailyNotesContracts.databaseEntry.TITLE_COL,
                DailyNotesContracts.databaseEntry.DESCRIPTION_COL,
                DailyNotesContracts.databaseEntry.DATE_COL,
                DailyNotesContracts.databaseEntry.PAGE_ID,
                DailyNotesContracts.databaseEntry.COLOR_INDEX
        };
        return new CursorLoader(getApplicationContext(), DailyNotesContracts.databaseEntry.CONTENT_URI, projection,
                null, null, DailyNotesContracts.databaseEntry.DATE_COL + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() == 0) {
            noTextView.setVisibility(View.VISIBLE);
        } else noTextView.setVisibility(View.GONE);
        if (isGrid) recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        else recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.swap(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swap(null);
    }

    @Override
    public void onPageClick(int position) {
        Intent intent = new Intent(getApplicationContext(), Note.class);
        Uri currentUri = ContentUris.withAppendedId(DailyNotesContracts.databaseEntry.CONTENT_URI, position);
        intent.setData(currentUri);
        intent.putExtra("ID", Integer.toString(position));
        startActivity(intent);

    }

    @Override
    public boolean onLongPageClick(int position) {
        Uri currentUri = ContentUris.withAppendedId(DailyNotesContracts.databaseEntry.CONTENT_URI, position);
        String selectionArgs[] = {String.valueOf(position)};
        int rows = getContentResolver().delete(
                DailyNotesContracts.databaseEntry.CONTENT_URI,
                DailyNotesContracts.databaseEntry.PAGE_ID + "=?",
                selectionArgs
        );
        return rows != 0;
    }
}