package com.example.ausgabenliste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    public void CreateNewEntry(View view) {
        Log.i("ListActivity","In CreateNewEntry");
        Intent intent = new Intent(this, EntryActivity.class);
        startActivity(intent);
    }

    public void BackToMain(View view) {
        Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnintent);
        finish();
    }
}