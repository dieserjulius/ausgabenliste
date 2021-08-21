package com.example.ausgabenliste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listMain;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ExpenditureList> expenditureListsOverview = new ArrayList<>();
        expenditureListsOverview.add(new ExpenditureList("Hello World!"));
        expenditureListsOverview.add(new ExpenditureList("Hello World."));
        expenditureListsOverview.add(new ExpenditureList("Hello World?"));

        listMain = findViewById(R.id.listMain);
        listMain.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExpenditureListAdapter(expenditureListsOverview);

        listMain.setLayoutManager(layoutManager);
        listMain.setAdapter(adapter);
    }

    public void CreateNewList(View view) {
        Log.i("MainActivity","In CreateNewList");
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}