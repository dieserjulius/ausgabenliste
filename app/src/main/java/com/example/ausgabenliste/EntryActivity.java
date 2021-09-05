package com.example.ausgabenliste;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EntryActivity extends AppCompatActivity {

    private ACTIONTYPE action = ACTIONTYPE.EDIT_DELETE;

    public static final String ACTION ="ACTION";
    public static final String DELETED ="DELETED";
    public static final String RESULT ="RESULT";
    public static final String ENTRYINDEX = "ENTRYINDEX";
    public static final String LISTNAME = "LISTNAME";
    public static final String LISTINDEX = "LISTINDEX";

    private int indexEntry = -1;
    private int indexList = -1;
    private String nameList = "";
    private ExpenditureList currentExList;

    EditText entryNameInput;
    EditText amountInput;

    /**
     * Baut grundlegende Elemente der View
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Intent intent = getIntent();

        int intentAction = intent.getIntExtra(ACTION, 0);
        action = ACTIONTYPE.getEnum(intentAction);
        nameList = intent.getStringExtra(LISTNAME);
        indexList = intent.getIntExtra(LISTINDEX, -1);

        ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
        currentExList = overview.getList(indexList);

        entryNameInput = findViewById(R.id.entryName);
        amountInput = findViewById(R.id.amount);

        if (action == ACTIONTYPE.EDIT_DELETE) {
            indexEntry = intent.getIntExtra(ENTRYINDEX, -1);

            EntryList list = currentExList.getEntryList();
            Entry currentEntry = list.getEntry(indexEntry);
            String currentEntryName = currentEntry.getEntryName();
            entryNameInput.setText(currentEntryName);
            String currentAmount = currentEntry.getAmountAsString(false);
            amountInput.setText(currentAmount);
        }

    }

    /**
     * Methode zum Speichern des Eintrags
     * @param view
     */

    public void SaveEntry(View view) {
        Entry ent = getEntryFromInput();

        String newEntryName = ent.getEntryName();
        double newAmount = ent.getAmount();

        boolean entryEmty = false;
        boolean amountEmty = false;

        if (newEntryName.equals("") || newAmount == 0){
            if (newEntryName.equals("")){
                entryEmty = true;
            }
            if (newAmount == 0){
                amountEmty = true;
            }
            alertEmptyInput(entryEmty, amountEmty);
            return;
        }

        // ChangeList Aufruf, falls Liste bereits vorhanden
        if (action == ACTIONTYPE.EDIT_DELETE) {
            EntryList list = currentExList.getEntryList();
            list.changeEntry(ent, indexEntry);
            list.saveInput(this, nameList);


            Intent returnintent = new Intent();
            returnintent.putExtra(EntryActivity.RESULT, true);
            setResult(Activity.RESULT_OK, returnintent);
            finish();
        }
        // AddList Aufruf, falls Liste noch nicht vorhanden
        else if (action == ACTIONTYPE.NEW) {
            EntryList list = currentExList.getEntryList();
            list.addEntry(ent);
            list.saveInput(this, nameList);

            Intent returnintent = new Intent();
            returnintent.putExtra(RESULT, true);
            setResult(Activity.RESULT_OK, returnintent);
            finish();
        }

        Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnintent);
        finish();
    }

    private void alertEmptyInput(boolean entryEmty, boolean amountEmty) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

        String title = "";
        String msg = "";

        if (entryEmty && amountEmty){
            title += "Leere Eingabefelder";
            msg += "Beide Eingabefelder sind leer.\n";
        } else if (entryEmty){
            title += "Leeres Bezeichnungsfeld";
            msg += "Das Eingabefeld für die Bezeichnung des Eintrags ist leer.\n";
        } else {
            title += "Leeres Wertefeld";
            msg += "Das Eingabefeld für den Wert des Eintrags ist leer.\n";
        }

        msg += "Bitte füllen Sie immer alle Felder aus.";

        alert.setTitle(title);
        alert.setMessage(msg);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("EntryActivity", "Ok pressed");
            }
        });
        alert.show();
    }

    public void deleteButtonClicked(View view) {
        // Alert, um betonen, dass der Eintrag endgültig gelöscht wird
        String msg = "Sind Sie sich sicher, dass Sie den Eintrag endgültig löschen wollen?";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Context context = this;

        alert.setTitle("Endgültig löschen");
        alert.setMessage(msg);

        // Bestätigungsbutton
        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EntryList list = currentExList.getEntryList();
                list.deleteEntry(indexEntry);
                list.saveInput(context, nameList);
                //adapter.notifyItemRemoved(indexEntry);
                Log.i("ListActivity", "Yes pressed");

                Intent returnintent = new Intent();
                returnintent.putExtra(RESULT, true);
                setResult(Activity.RESULT_OK, returnintent);
                finish();
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
    }

    private Entry getEntryFromInput() {
        String entryName = entryNameInput.getText().toString();
        String stringAmount = amountInput.getText().toString();
        Double amountValue;

        if (stringAmount.equals("")) {
            return new Entry(entryName, 0);
        } else {
             amountValue = Double.parseDouble(stringAmount);
        }
        double amount = amountValue.doubleValue();

        return new Entry(entryName, amount);
    }
}