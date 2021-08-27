package com.example.ausgabenliste;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
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
    private ExpenditureListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpenditureListsOverview.getInstance().loadInput(this);

        expenditureListsOverview = ExpenditureListsOverview.getInstance().getOverview();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK && result.getData() != null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        listMain = findViewById(R.id.listMain);
        listMain.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExpenditureListAdapter(expenditureListsOverview);

        listMain.setLayoutManager(layoutManager);
        listMain.setAdapter(adapter);

        adapter.setOnItemClickListener(new ExpenditureListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ExpenditureList exList = ExpenditureListsOverview.getInstance().getList(position);
                showListActivity(ACTIONTYPE.EDIT_DELETE, exList, position);
            }

            @Override
            public void onDeleteClick(int position) {
                deleteList(position);
            }
        });
    }

    private void deleteList(int position) {
        String msg = "Sind Sie sich sicher, dass Sie die Liste endgültig löschen wollen?";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Context context = this;

        alert.setTitle("Endgültig löschen");
        alert.setMessage(msg);

        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExpenditureListsOverview.getInstance().deleteList(position);
                ExpenditureListsOverview.getInstance().saveInput(context);
                adapter.notifyItemRemoved(position);
                Log.i("MainActivity", "Yes pressed");
            }
        });

        alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MainActivity", "No pressed");
            }
        });

        alert.show();
    }

    public void CreateNewList(View view) {
        ExpenditureList exList = new ExpenditureList();
        showListActivity(ACTIONTYPE.NEW, exList, -1);
    }

    private void showListActivity (ACTIONTYPE action, ExpenditureList exList, int position){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ExpenditureList.LST, exList);
        intent.putExtra(ListActivity.LISTINDEX, position);
        intent.putExtra(ListActivity.ACTION, action.ordinal());

        activityResultLauncher.launch(intent);
    }


}