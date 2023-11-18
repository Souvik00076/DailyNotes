package com.example.dailynotes.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.PrecomputedText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dailynotes.Adapters.AppExecutor;
import com.example.dailynotes.R;
import com.example.dailynotes.Data.DailyNotesContracts;
import com.example.dailynotes.databinding.ActivityNoteBinding;
import com.example.dailynotes.viewmodels.NoteModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executor;

public class Note extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri productUri;
    private static final int LOADER_NO = 2;
    public NoteModel noteModel;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoteBinding noteBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_note);
        noteModel = new NoteModel(getResources(), this, getContentResolver(), getIntent());
        noteBinding.setNotemodel(noteModel);
        noteBinding.setLifecycleOwner(this);
        if (null != getSupportActionBar()) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productUri = getIntent().getData();
        if (productUri != null) {
            LoaderManager.getInstance(this).initLoader(LOADER_NO, null, this);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED, new Intent());
                onBackPressed();
                return true;
            case R.id.Save_Note:
                noteModel.onSaveClick();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        return new CursorLoader(getApplicationContext(), productUri, projection,
                null, null, null);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        int titleColIndex = data.getColumnIndex(DailyNotesContracts.databaseEntry.TITLE_COL);
        int descpColIndex = data.getColumnIndex(DailyNotesContracts.databaseEntry.DESCRIPTION_COL);
        int colorIndex = data.getColumnIndex(DailyNotesContracts.databaseEntry.COLOR_INDEX);
        String color = data.getString(colorIndex);
        final String title = data.getString(titleColIndex);
        final String description = data.getString(descpColIndex);
        noteModel.setData(title, description, color);
        LoaderManager.getInstance(this).destroyLoader(LOADER_NO);
    }
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}