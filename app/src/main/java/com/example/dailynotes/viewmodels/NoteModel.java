package com.example.dailynotes.viewmodels;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;

import com.example.dailynotes.Adapters.AppExecutor;
import com.example.dailynotes.BR;
import com.example.dailynotes.Data.DailyNotesContracts;
import com.example.dailynotes.R;

import java.util.concurrent.Executor;

public class NoteModel extends BaseObservable {
    private ContentValues contentValues;
    private int pickUpValues;
    private String[] colorValues, textColors;
    private Context _this;
    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    private final ObservableInt backgroundObservable = new ObservableInt();
    private Intent intent;
    private ContentResolver resolver;

    public NoteModel(Resources resources, Context context, ContentResolver resolver, Intent intent) {
        contentValues = new ContentValues();
        this.intent = intent;
        this.resolver = resolver;
        _initConfig(resources, context);


    }

    @Bindable
    public int getBackgroundObservable() {
        return backgroundObservable.get();
    }

    public void setBackgroundObservable(int val) {
        backgroundObservable.set(val);
        notifyPropertyChanged(BR.backgroundObservable);
    }

    @Bindable
    public String getTitle() {
        return contentValues.getAsString(DailyNotesContracts.databaseEntry.TITLE_COL);
    }

    @Bindable
    public String getDescription() {
        return contentValues.getAsString(DailyNotesContracts.databaseEntry.DESCRIPTION_COL);
    }

    public void setTitle(String title) {
        System.out.println("title working");
        contentValues.put(DailyNotesContracts.databaseEntry.TITLE_COL, title);
        notifyPropertyChanged(BR.title);
    }

    public void setDescription(String description) {
        System.out.println("description working");
        contentValues.put(DailyNotesContracts.databaseEntry.DESCRIPTION_COL, description);
        notifyPropertyChanged(BR.description);
    }

    public void setColor() {
        Log.i("Here", "Color Changed");
        setBackgroundObservable(pickUpValues++);
        pickUpValues %= colorValues.length;
    }

    public void onSaveClick() {
        insertData();
    }

    private void insertData() {
        long unixTime = System.currentTimeMillis() / 1000L;
        contentValues.put(DailyNotesContracts.databaseEntry.DATE_COL, unixTime);
        contentValues.put(DailyNotesContracts.databaseEntry.COLOR_INDEX, colorValues[pickUpValues]);
        System.out.println("InsertData");
        insertOrUpdate();
    }

    private void _initConfig(Resources resources, Context context) {
        pickUpValues = 0;
        colorValues = resources.getStringArray(R.array.color_values);
        textColors = resources.getStringArray(R.array.text_colors);
        this._this = context;
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizer = SpeechRecognizer.createSpeechRecognizer(_this);
        recognizer.setRecognitionListener((RecognitionListener) _this);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    }

    public void setData(String title, String description, String color) {
        for (int i = 0; i < colorValues.length; i++)
            if (colorValues[i].equals(color))
                pickUpValues = i;
        setTitle(title);
        setDescription(description);
    }

    private void insertOrUpdate() {
        System.out.println(contentValues.get(DailyNotesContracts.databaseEntry.DESCRIPTION_COL));
        System.out.println(contentValues.get(DailyNotesContracts.databaseEntry.TITLE_COL));
        System.out.println("InsertOrUpdate");

        Executor executor = AppExecutor.getInstance().getDiskIO();
        if (null == intent.getData()) {
            executor.execute(() -> resolver.insert(DailyNotesContracts.databaseEntry.CONTENT_URI, contentValues));

        } else {
            String productID = intent.getStringExtra("ID");
            executor.execute(() -> {
                contentValues.put(DailyNotesContracts.databaseEntry.PAGE_ID, productID);
                resolver.update(intent.getData(), contentValues, null, null);
            });
        }
    }
}
