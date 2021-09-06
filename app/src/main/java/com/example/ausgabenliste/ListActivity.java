package com.example.ausgabenliste;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView tvTotalAmountView;
    private String originallyListName;

    private int indexList = -1;

   /**
     * Baut grundlegende Elemente der View.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Übergabe der eingegebenen Daten
        Intent intent = getIntent();

        int intentAction = intent.getIntExtra(ACTION, 0);
        action = ACTIONTYPE.getEnum(intentAction);

        listNameInput = findViewById(R.id.listName);
        tvTotalAmountView = findViewById(R.id.tvTotalAmountView);

        indexList = intent.getIntExtra(LISTINDEX, -1);

        ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
        ExpenditureList currentList = overview.getList(indexList);

        entryList = currentList.getEntryList().getList();

        // Ursprünglicher Name wird für das Renaming global hinterlegt
        originallyListName = currentList.getListName();
        listNameInput.setText(originallyListName);
        currentList.getEntryList().loadInput(this, originallyListName);

        // Bei eingehendem Ergebnis wird benachrichtigt, dass die Daten geändert wurden
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK && result.getData() != null){
                            adapter.notifyDataSetChanged();
                            calculateTotalAmount();
                        }
                    }
                });

        buildRecyclerView();
        calculateTotalAmount();
    }

    /**
     * Berechnet den Betrag aller Werte dieser Liste
     */

    private void calculateTotalAmount() {
        double totalAmount = 0.00;
        for (Entry ent : entryList) {
            totalAmount += ent.getAmount();
        }
        // Ausgabe mit 2 Nachkommastellen und dem €-Symbol
        tvTotalAmountView.setText(String.format("%,.2f", totalAmount) +  "€");
    }

    /**
     * Baut die RecyclerView auf
     */

    private void buildRecyclerView() {
        // RecyclerView wird aufgebaut
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

    /**
     * Kreiert einen neuen Entry und ruft die "showEntryActivity" auf
     * @param view
     */

    public void createNewEntry(View view) {
        Entry entry = new Entry();
        showEntryActivity(ACTIONTYPE.NEW, entry, -1);
    }

    /**
     * Bereitet alles für einen Aufruf der EntryActivity vor und ruft diese auf
     * @param action ACTIONTYPE, der besagt welche Form der
     *               EntryActivity ausgeführt werden soll
     * @param entry Entry, die verändert werden soll
     * @param position Position des zu bearbeitenden Entrys
     */

    private void showEntryActivity(ACTIONTYPE action, Entry entry, int position) {
        String listname = getListnameFromInput();
        ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();;

        // Überprüfung, ob der Name schon existiert oder leer ist
        boolean listNameAlreadyExist = false;
        for (int i = 0; i < overview.getSize(); i++) {
            String currentListname = overview.getList(i).getListName();
            if (currentListname.equals(listname) && indexList != i){
                listNameAlreadyExist = true;
            }
        }
        if (listNameAlreadyExist || listname.equals("")){
            alertIllegalName();
            return;
        }

        // Füllung des Intents für die Weitergabe
        Intent intent = new Intent(this, EntryActivity.class);
        intent.putExtra(Entry.ENT, entry);
        intent.putExtra(EntryActivity.ENTRYINDEX, position);
        intent.putExtra(EntryActivity.LISTNAME, listname);
        intent.putExtra(EntryActivity.LISTINDEX, indexList);
        intent.putExtra(EntryActivity.ACTION, action.ordinal());

        ExpenditureList lst = getExListFromInput();

        changeAndSave(lst);

        activityResultLauncher.launch(intent);
    }

    /**
     * Zeigt einen Alert an, falls ein nicht zulässiger Name eingegeben wurde
     */

    private void alertIllegalName() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

        alert.setTitle("Unzulässiger Name");
        alert.setMessage("Das Eingabefeld ist leer oder der Name ist bereits vorhanden. Bitte einen anderen Namen wählen.");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("ListActivity", "Ok pressed");
            }
        });
        alert.show();
    }

    /**
     * Bereitet alles vor und kehrt wieder zur MainActivity zurück
     * @param view
     */

    public void backToMain(View view) {
        String listname = getListnameFromInput();
        ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();

        // Überprüfung, ob der Name schon existiert oder leer ist
        boolean listNameAlreadyExist = false;
        for (int i = 0; i < overview.getSize(); i++) {
            String currentListname = overview.getList(i).getListName();
            if (currentListname.equals(listname) && indexList != i) {
                listNameAlreadyExist = true;
            }
        }
        if (listNameAlreadyExist || listname.equals("")) {
            alertIllegalName();
            return;
        }

        // Falls der Name der Liste geändert wurde
        if (!originallyListName.equals(listname)){
            EntryList currentList = overview.getList(indexList).getEntryList();
            // Datei mit altem Listennamen wird gelöscht und eine Datei mit dem neuen
            // Namen wird angefertigt, damit keine falschen Dateien geladen werden
            currentList.renameFile(this, originallyListName, listname);
        }

        ExpenditureList lst = getExListFromInput();

        changeAndSave(lst);


        // Füllung des Intents für die Rückgabe
        Intent returnintent = new Intent();
        returnintent.putExtra(EntryActivity.RESULT, true);
        setResult(Activity.RESULT_OK,returnintent);
        finish();

    }

    /**
     * Enthält Aufrufe zum Ändern und Speichern der List
     * @param lst List die geändert und gespeichert werden soll
     */

    private void changeAndSave(ExpenditureList lst) {
        ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
        overview.changeList(lst, indexList);
        overview.saveInput(this);
    }

    /**
     * Nimmt die Eingabe für den Namen und gibt einen String lediglich für den Namen zurück
     * @return Neue ExpenditureList aus den Eingaben
     */

    private String getListnameFromInput() {
        return listNameInput.getText().toString();
    }

    /**
     * Nimmt die Eingaben und erstellt daraus eine neue ExpenditureList
     * @return Neue ExpenditureList aus den Eingaben
     */

    private ExpenditureList getExListFromInput() {
        String listName = listNameInput.getText().toString();
        return new ExpenditureList(listName);
    }
}