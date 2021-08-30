package com.example.ausgabenliste;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ListActivity extends AppCompatActivity {

    private ACTIONTYPE action = ACTIONTYPE.EDIT_DELETE;

    public static final String ACTION ="ACTION";
    public static final String RESULT ="RESULT";
    public static final String LISTINDEX = "LISTINDEX";

    private EditText listNameInput;

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

        // Falls eine vorhandene Liste bearbeitet werden soll, wird diese heir geholt
        if (action == ACTIONTYPE.EDIT_DELETE) {
            indexList = intent.getIntExtra(LISTINDEX, -1);

            ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
            ExpenditureList currentList = overview.getList(indexList);
            String currentListName = currentList.getListName();
            listNameInput.setText(currentListName);
        }
    }

    /**
     * Erstellt einen neuen Eintrag und rudt die EntryActivity auf
     * @param view
     */

    public void CreateNewEntry(View view) {
        // Hier wird noch nichts Erstellt
        // Provisorisch, da das Programm noch nicht fertig ist
        Log.i("ListActivity","In CreateNewEntry");
        Intent intent = new Intent(this, EntryActivity.class);
        startActivity(intent);
    }

    /**
     * Kehrt zur MainActivity zurück und speichert alle eingegebenen Daten
     * @param view
     */

    public void BackToMain(View view) {
        ExpenditureList lst = getExListFromInput();

            // ChangeList Aufruf, falls Liste bereits vorhanden
            if (action==ACTIONTYPE.EDIT_DELETE) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                overview.changeList(lst, indexList);
                overview.saveInput(this);

                Intent returnintent = new Intent();
                returnintent.putExtra(RESULT, true);
                setResult(Activity.RESULT_OK,returnintent);
                finish();
            }
            // AddList Aufruf, falls Liste noch nicht vorhanden
            else if (action==ACTIONTYPE.NEW) {
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

    private ExpenditureList getExListFromInput() {
        String listName = listNameInput.getText().toString();
        return new ExpenditureList(listName);
    }
}