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
import android.widget.EditText;
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
    private ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * Baut grundlegende Elemente der View.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpenditureListsOverview.getInstance().loadInput(this);

        expenditureListsOverview = ExpenditureListsOverview.getInstance().getOverview();

        // Bei eingehendem Ergebnis wird benachrichtigt, dass die Daten geändert wurden
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

    /**
     * Baut die RecyclerView auf
     */

    private void buildRecyclerView() {
        // RecyclerView wird aufgebaut
        listMain = findViewById(R.id.listMain);
        listMain.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExpenditureListAdapter(expenditureListsOverview);

        listMain.setLayoutManager(layoutManager);
        listMain.setAdapter(adapter);

        adapter.setOnItemClickListener(new ExpenditureListAdapter.OnItemClickListener() {

            // Wenn auf das Item geklickt wird
            @Override
            public void onItemClick(int position) {
                ExpenditureList exList = ExpenditureListsOverview.getInstance().getList(position);
                showListActivity(ACTIONTYPE.EDIT_DELETE, exList, position);
            }

            // Wenn auf "Löschen" geklickt wird
            @Override
            public void onDeleteClick(int position) {
                deleteList(position);
            }
        });
    }

    /**
     * Löscht an einer gewünschten Position die Liste
     * und sichert das ganze mit einem Alert zur Bestätigung ab
     * @param position Gewünschte Position
     */

    private void deleteList(int position) {
        // Alert, um betonen, dass die Liste endgültig gelöscht wird
        String msg = "Sind Sie sich sicher, dass Sie die Liste endgültig löschen wollen?";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        Context context = this;

        alert.setTitle("Endgültig löschen");
        alert.setMessage(msg);

        // Bestätigungsbutton
        alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExpenditureListsOverview.getInstance().deleteList(position);
                ExpenditureListsOverview.getInstance().saveInput(context);
                adapter.notifyItemRemoved(position);
                Log.i("MainActivity", "Yes pressed");
            }
        });

        // Button zum abbrechen
        alert.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MainActivity", "No pressed");
            }
        });

        alert.show();
    }

    /**
     * Methode um "showListActivity" aufzuraufen, um eine neue Liste zu erstellen
     * @param view
     */

    public void CreateNewList(View view) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

        Context context = this;

        alert.setTitle("Namen eingeben");
        alert.setMessage("Bitte geben Sie den gewünschten Namen Ihrer Liste ein.");

        final EditText newListNameInput = new EditText(this);
        newListNameInput.setMaxWidth(50);
        alert.setView(newListNameInput);

        alert.setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newListName = newListNameInput.getText().toString();
                boolean listNameAlreadyExist = false;
                for (ExpenditureList lst : expenditureListsOverview) {
                    if (lst.getListName().equals(newListName)){
                        listNameAlreadyExist = true;
                    }
                }
                if (listNameAlreadyExist || newListName.equals("")){
                    alertIllegalName();
                    return;
                }
                ExpenditureList exList = new ExpenditureList(newListName);
                ExpenditureListsOverview.getInstance().addList(exList);
                ExpenditureListsOverview.getInstance().saveInput(context);
                adapter.notifyItemInserted(adapter.getItemCount()+1);
            }
        });
        alert.show();
        //ExpenditureList exList = new ExpenditureList();
        //showListActivity(ACTIONTYPE.NEW, null, -1);
    }

    private void alertIllegalName() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

        alert.setTitle("Unzulässiger Name");
        alert.setMessage("Das Eingabefeld ist leer oder der Name ist bereits vorhanden. Bitte einen anderen Namen wählen.");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("MainActivity", "Ok pressed");
            }
        });
        alert.show();
    }

    /**
     * Ruft die "ListActivity" auf, um eine List zu erstellen oder eine List zu bearbeiten
     * @param action Wert, ob Liste neu erstellt oder geändert werden soll
     * @param exList Instanz der ExpenditureList, die modifiziert werden soll
     * @param position Position der Liste, die modifiziert werden soll
     */

    private void showListActivity (ACTIONTYPE action, ExpenditureList exList, int position){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ExpenditureList.LST, exList);
        intent.putExtra(ListActivity.LISTINDEX, position);
        intent.putExtra(ListActivity.ACTION, action.ordinal());

        activityResultLauncher.launch(intent);
    }


}