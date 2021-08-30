package com.example.ausgabenliste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EntryActivity extends AppCompatActivity {

    /**
     * Baut grundlegende Elemente der View
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
    }

    /**
     * Methode zum Speichern des Eintrags
     * @param view
     */

    public void SaveEntry(View view) {
        // Hier wird noch nichts gespeichert
        // Provisorisch, da das Programm noch nicht fertig ist
        Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnintent);
        finish();
    }
}