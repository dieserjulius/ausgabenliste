package com.example.ausgabenliste;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ExpenditureList> expenditureListsOverview;

    private RecyclerView listMain;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK && result.getData() != null){
                            //TODO Code nach Empfang des Ergebnisses
                            Intent data = result.getData();
                        }
                    }
                });

        createList();
        buildRecyclerView();


    }

    private void createList() {
        expenditureListsOverview = new ArrayList<>();
        expenditureListsOverview.add(new ExpenditureList("Hello World!"));
        expenditureListsOverview.add(new ExpenditureList("Hello World."));
        expenditureListsOverview.add(new ExpenditureList("Hello World?"));
    }

    private void buildRecyclerView() {
        listMain = findViewById(R.id.listMain);
        listMain.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExpenditureListAdapter(expenditureListsOverview);

        listMain.setLayoutManager(layoutManager);
        listMain.setAdapter(adapter);
    }

    public void CreateNewList(View view) {
        Log.i("MainActivity","In CreateNewList");
        ExpenditureList exList = new ExpenditureList();
        showListActivity(ACTIONTYPE.NEW, exList, -1);
    }

    private void showListActivity (ACTIONTYPE action, ExpenditureList exList, int position){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ExpenditureList.LST, exList);  // // jeweils  hashKey+wert
        intent.putExtra(ListActivity.LISTINDEX, position);
        intent.putExtra(ListActivity.ACTION, action.ordinal());

        activityResultLauncher.launch(intent);

        //startActivityForResult(intent, sendenr);
    }
}