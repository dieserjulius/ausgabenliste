package com.example.ausgabenliste;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private ArrayList<Entry> entryList;

    private RecyclerView entryListView;
    private EntryAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    
    private ACTIONTYPE action = ACTIONTYPE.EDIT_DELETE;

    public static final String ACTION ="ACTION";
    public static final String RESULT ="RESULT";
    public static final String LISTINDEX = "LISTINDEX";

    private EditText listNameInput;

    private int indexList = -1;
    private boolean alreadyAdded = true;



    /**
     * Baut grundlegende Elemente der View.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        /*String listname = getListnameFromInput();
        EntryList.getInstance().loadInput(this, listname);*/

        //entryList = EntryList.getInstance().getList();

        // Übergabe der eingegebenen Daten
        Intent intent = getIntent();

        int intentAction = intent.getIntExtra(ACTION, 0);
        action = ACTIONTYPE.getEnum(intentAction);

        listNameInput = findViewById(R.id.listName);

        if (action == ACTIONTYPE.NEW){
            ExpenditureList lst = getExListFromInput();
            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            overview.addList(lst);
            overview.saveInput(this);

            entryList = lst.getEntryList().getList();
        }
        // Falls eine vorhandene Liste bearbeitet werden soll, wird diese heir geholt
        else if (action == ACTIONTYPE.EDIT_DELETE) {
            indexList = intent.getIntExtra(LISTINDEX, -1);

            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            ExpenditureList currentList = overview.getList(indexList);

            entryList = currentList.getEntryList().getList();

            String currentListName = currentList.getListName();
            listNameInput.setText(currentListName);
            currentList.getEntryList().loadInput(this, currentListName);
        }

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
        entryListView = findViewById(R.id.listEntry);

        entryListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new EntryAdapter(entryList);

        entryListView.setLayoutManager(layoutManager);
        entryListView.setAdapter(adapter);

        adapter.setOnItemClickListener(new EntryAdapter.OnItemClickListener() {

            // Wenn auf das Item geklickt wird
            @Override
            public void onItemClick(int position) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                ExpenditureList currentList = overview.getList(indexList);
                Entry entry = currentList.getEntryList().getEntry(position);
                showEntryActivity(ACTIONTYPE.EDIT_DELETE, entry, position);
            }
        });
    }


    public void createNewEntry(View view) {
        Entry entry = new Entry();
        showEntryActivity(ACTIONTYPE.NEW, entry, -1);
    }

    private void showEntryActivity(ACTIONTYPE action, Entry entry, int position) {
        String listname = getListnameFromInput();
        Intent intent = new Intent(this, EntryActivity.class);
        intent.putExtra(Entry.ENT, entry);
        intent.putExtra(EntryActivity.ENTRYINDEX, position);
        intent.putExtra(EntryActivity.LISTNAME, listname);
        intent.putExtra(EntryActivity.LISTINDEX, indexList);
        intent.putExtra(EntryActivity.ACTION, action.ordinal());


        ExpenditureList lst = getExListFromInput();

        // ChangeList Aufruf, falls Liste bereits vorhanden
        //if (action == ACTIONTYPE.EDIT_DELETE) {
            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            overview.changeList(lst, indexList);
            overview.saveInput(this);
        //}
        // AddList Aufruf, falls Liste noch nicht vorhanden
        /*else if (action == ACTIONTYPE.NEW) {
            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            overview.addList(lst);
            overview.saveInput(this);
            alreadyAdded = true;
        }*/

        activityResultLauncher.launch(intent);
    }

    public void backToMain(View view) {
        ExpenditureList lst = getExListFromInput();

            // ChangeList Aufruf, falls Liste bereits vorhanden
            //if (action == ACTIONTYPE.EDIT_DELETE) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                overview.changeList(lst, indexList);
                overview.saveInput(this);

                Intent returnintent = new Intent();
                returnintent.putExtra(EntryActivity.RESULT, true);
                setResult(Activity.RESULT_OK,returnintent);
                finish();
            //}
            // AddList Aufruf, falls Liste noch nicht vorhanden
            /*else if (action == ACTIONTYPE.NEW && !alreadyAdded) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                overview.addList(lst);
                overview.saveInput(this);

                Intent returnintent = new Intent();
                returnintent.putExtra(RESULT, true);
                setResult(Activity.RESULT_OK, returnintent);
                finish();
            }

        Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnintent);
        finish();*/
    }

    /**
     * Nimmt die Eingaben und erstellt daraus eine neue ExpenditureList
     * @return Neue ExpenditureList aus den Eingaben
     */

    private String getListnameFromInput() {
        return listNameInput.getText().toString();
    }

    private ExpenditureList getExListFromInput() {
        String listName = listNameInput.getText().toString();
        return new ExpenditureList(listName);
    }
}