package com.example.ausgabenliste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainActivity extends AppCompatActivity {

    private ListView listMain = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listMain = (ListView) findViewById(R.id.listMain);
    }

    public void CreateNewList(View view) {
        Log.i("MainActivity","In CreateNewList");
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}