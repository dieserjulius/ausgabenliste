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
    public static final String RESULT ="RESULT";
    public static final String ENTRYINDEX = "ENTRYINDEX";
    public static final String LISTNAME = "LISTNAME";

    private int indexEntry = -1;
    private String nameList = "";

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

        entryNameInput = findViewById(R.id.entryName);
        amountInput = findViewById(R.id.amount);

        if (action == ACTIONTYPE.EDIT_DELETE) {
            indexEntry = intent.getIntExtra(ENTRYINDEX, -1);

            EntryList list = EntryList.getInstance();
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
        // Hier wird noch nichts gespeichert
        // Provisorisch, da das Programm noch nicht fertig ist
        /*Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnintent);
        finish();*/

        Entry ent = getEntryFromInput();

        // ChangeList Aufruf, falls Liste bereits vorhanden
        if (action == ACTIONTYPE.EDIT_DELETE) {
            EntryList list = EntryList.getInstance();
            list.changeEntry(ent, indexEntry);
            list.saveInput(this, nameList);

            Intent returnintent = new Intent();
            returnintent.putExtra(EntryActivity.RESULT, true);
            setResult(Activity.RESULT_OK, returnintent);
            finish();
        }
        // AddList Aufruf, falls Liste noch nicht vorhanden
        else if (action == ACTIONTYPE.NEW) {
            EntryList list = EntryList.getInstance();
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
                EntryList.getInstance().deleteEntry(indexEntry);
                EntryList.getInstance().saveInput(context, nameList);
                //adapter.notifyItemRemoved(indexEntry);
                Log.i("ListActivity", "Yes pressed");
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
        double amount = 0;

        Double amountValue = Double.parseDouble(stringAmount);
        if (amountValue == null) {
            //Basis.alertDialogOk(this,"Fehlerhafte Matrikelnummer: "+str_matrnr,"Fehler");
            return null;
        }
        amount = amountValue.doubleValue();

        return new Entry(entryName, amount);
    }
}