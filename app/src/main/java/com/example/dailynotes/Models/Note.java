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

public class Note extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, RecognitionListener {
    private EditText showTitle, showDescription;
    private Uri productUri;
    private String productID;
    private LinearLayout layout;
    private static final int LOADER_NO = 2;
    private FloatingActionButton changeColor;
    private FloatingActionButton speech;
    private String[] colorValues;
    private String[] textColors;
    private int pickUpValues;

    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    public NoteModel noteModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityNoteBinding noteBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_note);
        noteModel = new NoteModel(getResources(), this);
        noteBinding.setNotemodel(noteModel);
        noteBinding.setLifecycleOwner(this);
        /*
        setContentView(R.layout.activity_note);
        noteModel=new NoteModel()

        if (null != getSupportActionBar()) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        productUri = getIntent().getData();
        if (productUri != null) {
            productID = getIntent().getStringExtra("ID");
            LoaderManager.getInstance(this).initLoader(LOADER_NO, null, this);
        }

        changeColor.setOnClickListener(view -> {
            pickUpValues++;
            if (pickUpValues == colorValues.length) pickUpValues = 0;
            setColor(pickUpValues);
        });
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertSpeech();
            }
        });
    */

    }

    private void convertSpeech() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            onRecordAudioPermissionGranted();
        } else {
            int PERMISSIONS_REQUEST = 1;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST);
        }

    }

    private void onRecordAudioPermissionGranted() {
        recognizer.startListening(recognizerIntent);
    }

    public void setColor(int pickUpValues) {
        layout.setBackgroundColor(Color.parseColor(colorValues[pickUpValues]));
        showTitle.setHintTextColor(Color.parseColor(textColors[pickUpValues]));
        showTitle.setTextColor(Color.parseColor(textColors[pickUpValues]));
        showDescription.setHintTextColor(Color.parseColor(textColors[pickUpValues]));
        showDescription.setTextColor(Color.parseColor(textColors[pickUpValues]));
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

                /*if (getValidityChecked()) {
                    Toast.makeText(this, "Title Missing", Toast.LENGTH_SHORT).show();
                } else {
                    insertOrUpdate();
                    onBackPressed();
                }*/
                noteModel.onSaveClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertOrUpdate() {
        ContentValues values = insertData();
        int rows = 0;
        Executor executor = AppExecutor.getInstance().getDiskIO();
        if (null == productUri) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Uri newUri = getContentResolver().insert(DailyNotesContracts.databaseEntry.CONTENT_URI, values);
                }
            });

        } else {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    values.put(DailyNotesContracts.databaseEntry.PAGE_ID, productID);
                    getContentResolver().update(productUri, values, null, null);

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {
            recognizer.destroy();
            recognizer = null;
        }
    }

    private ContentValues insertData() {
        ContentValues values = new ContentValues();
        String title = showTitle.getText().toString().trim();
        String description = showDescription.getText().toString().trim();
        long unixTime = System.currentTimeMillis() / 1000L;
        values.put(DailyNotesContracts.databaseEntry.TITLE_COL, title);
        values.put(DailyNotesContracts.databaseEntry.DESCRIPTION_COL, description);
        values.put(DailyNotesContracts.databaseEntry.DATE_COL, unixTime);
        values.put(DailyNotesContracts.databaseEntry.COLOR_INDEX, colorValues[pickUpValues]);
        return values;
    }

    private boolean getValidityChecked() {
        return showTitle.getText().toString().isEmpty();
    }

    private void initViews() {
        pickUpValues = 0;
        layout = findViewById(R.id.note);
        showDescription = layout.findViewById(R.id.descp);
        showTitle = layout.findViewById(R.id.title);
        changeColor = findViewById(R.id.changeColor);
        colorValues = getResources().getStringArray(R.array.color_values);
        textColors = getResources().getStringArray(R.array.text_colors);
        speech = (FloatingActionButton) findViewById(R.id.speech);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(this);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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
        showDescription.setText(data.getString(descpColIndex));
        showTitle.setText(data.getString(titleColIndex));
        String color = data.getString(colorIndex);
        for (int i = 0; i < colorValues.length; i++)
            if (colorValues[i].equals(color))
                pickUpValues = i;
        //setColor(pickUpValues);

        LoaderManager.getInstance(this).destroyLoader(LOADER_NO);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        recognizer.stopListening();
    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {

        List<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String partial : result) {
            showDescription.append(partial + " ");
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}