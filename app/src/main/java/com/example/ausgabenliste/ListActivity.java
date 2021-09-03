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

    //TODO Bugs: Pro Änderung der Entrys eine neue Liste
    // Alle Einträge beim Erstellen die Gleichen,
    // Löschen aktualisiert die Liste nicht sofort

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

        entryList = EntryList.getInstance().getList();

        // Übergabe der eingegebenen Daten
        Intent intent = getIntent();

        int intentAction = intent.getIntExtra(ACTION, 0);
        action = ACTIONTYPE.getEnum(intentAction);

        listNameInput = findViewById(R.id.listName);

        // Falls eine vorhandene Liste bearbeitet werden soll, wird diese heir geholt
        if (action == ACTIONTYPE.EDIT_DELETE) {
            indexList = intent.getIntExtra(LISTINDEX, -1);

            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            ExpenditureList currentList = overview.getList(indexList);
            String currentListName = currentList.getListName();
            listNameInput.setText(currentListName);
            EntryList.getInstance().loadInput(this, currentListName);
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
                Entry entry = EntryList.getInstance().getEntry(position);
                showEntryActivity(ACTIONTYPE.EDIT_DELETE, entry, position);
            }

            // Wenn auf "Löschen" geklickt wird
            /*@Override
            public void onDeleteClick(int position) {
                deleteList(position);
            }*/
        });
    }

    /*private void deleteList(int position) {
        // Alert, um betonen, dass der Eintrag endgültig gelöscht wird
        String msg = "Sind Sie sich sicher, dass Sie den Eintrag endgültig löschen wollen?";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Context context = this;

        String listname = getListnameFromInput();

        alert.setTitle("Endgültig löschen");
        alert.setMessage(msg);

        // Bestätigungsbutton
        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EntryList.getInstance().deleteEntry(position);
                EntryList.getInstance().saveInput(context, listname);
                adapter.notifyItemRemoved(position);
                Log.i("ListActivity", "Yes pressed");
            }
        });

        // Button zum abbrechen
        alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("ListActivity", "No pressed");
            }
        });

        alert.show();
    }*/

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
        intent.putExtra(EntryActivity.ACTION, action.ordinal());



        ExpenditureList lst = getExListFromInput();

        // ChangeList Aufruf, falls Liste bereits vorhanden
        if (action == ACTIONTYPE.EDIT_DELETE) {
            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            overview.changeList(lst, indexList);
            overview.saveInput(this);
        }
        // AddList Aufruf, falls Liste noch nicht vorhanden
        else if (action == ACTIONTYPE.NEW) {
            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            overview.addList(lst);
            overview.saveInput(this);
        }



        activityResultLauncher.launch(intent);
    }

    public void backToMain(View view) {
        ExpenditureList lst = getExListFromInput();

            // ChangeList Aufruf, falls Liste bereits vorhanden
            if (action == ACTIONTYPE.EDIT_DELETE) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                overview.changeList(lst, indexList);
                overview.saveInput(this);

                Intent returnintent = new Intent();
                returnintent.putExtra(EntryActivity.RESULT, true);
                setResult(Activity.RESULT_OK,returnintent);
                finish();
            }
            // AddList Aufruf, falls Liste noch nicht vorhanden
            else if (action == ACTIONTYPE.NEW) {
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
        finish();
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