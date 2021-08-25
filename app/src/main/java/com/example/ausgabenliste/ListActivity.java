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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();

        int intentAction = intent.getIntExtra(ACTION, 0);
        action = ACTIONTYPE.getEnum(intentAction);

        if (action==ACTIONTYPE.EDIT_DELETE) {
            indexList = intent.getIntExtra(LISTINDEX, -1);
        }

        listNameInput = findViewById(R.id.listName);
    }

    public void CreateNewEntry(View view) {
        Log.i("ListActivity","In CreateNewEntry");
        Intent intent = new Intent(this, EntryActivity.class);
        startActivity(intent);
    }

    public void BackToMain(View view) {
        ExpenditureList lst = getExListFromInput();
        // Eigentlich nicht nötig, da ExList nie null
        /*if (lst==null) {
            return;
        }
        else {*/
            if (action==ACTIONTYPE.EDIT_DELETE) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                overview.changeList(lst,indexList);
                overview.getInstance().saveInput(this);

                Intent returnintent = new Intent(); // also ein Auftrag, etwas zu tun
                returnintent.putExtra(RESULT, true); // bräuchte man nicht unbedingt
                setResult(Activity.RESULT_OK,returnintent);
                finish();
            }
            else if (action==ACTIONTYPE.NEW) {
                ExpenditureListsOverview overview = ExpenditureListsOverview.getInstance();
                overview.addList(lst);
                overview.getInstance().saveInput(this);

                Intent returnintent = new Intent();
                returnintent.putExtra(RESULT, true);
                setResult(Activity.RESULT_OK, returnintent);
                finish();
            }
            //}
        Intent returnintent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnintent);
        finish();
    }

    private ExpenditureList getExListFromInput() {
        String listName = listNameInput.getText().toString();
        return new ExpenditureList(listName);
    }
}