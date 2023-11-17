package com.example.dailynotes.viewmodels;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;

import com.example.dailynotes.BR;
import com.example.dailynotes.R;

public class NoteModel extends BaseObservable {
    private ContentValues contentValues;
    private int pickUpValues;
    private String[] colorValues, textColors;
    private Context _this;
    private SpeechRecognizer recognizer;
    private Intent recognizerIntent;
    public final ObservableInt backgroundObservable = new ObservableInt();

    public NoteModel(Resources resources, Context context) {
        contentValues = new ContentValues();
        _initConfig(resources, context);
    }
    @Bindable
    public int getBackgroundObservable() {
        return backgroundObservable.get();
    }
    public void setBackgroundObservable(int val){
        backgroundObservable.set(val);
        notifyPropertyChanged(BR.backgroundObservable);
    }
    @Bindable
    public String getTitle() {
        return contentValues.getAsString("title");
    }

    @Bindable
    public String getDescription() {
        return contentValues.getAsString("description");
    }

    public void setTitle(String title) {
        contentValues.put("title", title);
        notifyPropertyChanged(BR.title);
    }

    public void setDescription(String description) {
        contentValues.put("description", description);
        notifyPropertyChanged(BR.description);
    }

    public void setColor() {
        Log.i("Here","Color Changed");
        setBackgroundObservable(pickUpValues++);
        pickUpValues %= colorValues.length;
    }

    public void onSaveClick() {
        System.out.println("On saved called");
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
}
