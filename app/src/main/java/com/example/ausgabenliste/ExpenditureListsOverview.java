package com.example.ausgabenliste;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class  ExpenditureListsOverview {

    private final String FILENAME = "expendditurelists.bin";

    public ArrayList<ExpenditureList> overview = new ArrayList<>();

    private static ExpenditureListsOverview instance = new ExpenditureListsOverview();

    /**
     * Leerer Konstruktor
     */

    public ExpenditureListsOverview() {
    }

    /**
     * Gibt die Instanz der ExpenditureListOverview zurück
     * @return Instanz der ExpenditureListOverview
     */

    public static ExpenditureListsOverview getInstance(){
        return instance;
    }

    /**
     * Fügt eine Liste der Overview hinzu
     * @param lst Liste, die hinzugefügt werden soll
     */

    public void addList(ExpenditureList lst){
        overview.add(lst);
    }

    /**
     * Löscht eine Liste aus der Overview
     * @param position Position der zu löschenden Liste
     */

    public void deleteList(int position){
        // Überprüfung, ob Position existiert
        if (position >= 0 && position < overview.size()) {
            overview.remove(position);
        }
        else {
            Log.e("ExpenditureListsOverview", "Position of List out of Bounds");
        }

    }

    /**
     * Methode zum Speichern
     * @param context
     */

    public void saveInput(Context context) {
        try {
            // Vorbereitung
            FileOutputStream fout = context.openFileOutput(FILENAME, MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.writeInt(overview.size());
            Log.i("ExpenditureListsOverview", "Saving List to File ok");

            // Aufruf der Save-Methode für jede einzelne ExpenditureList
            for (ExpenditureList lst : overview) {
                lst.save(dout);
            }

            dout.close();
            fout.close();
        }
        catch (IOException e) {
            Log.e("ExpenditureListsOverview", "saveError in saveInput");
        }
    }

    /**
     * Methode zum Laden
     * @param context
     */

    public void loadInput(Context context) {
        try {
            // Vorbereitung
            overview.clear();
            FileInputStream fin = context.openFileInput(FILENAME);
            DataInputStream din = new DataInputStream(fin);
            int n = din.readInt();
            Log.i("ExpenditureListsOverview", "Loading List from File ok");
            ExpenditureList lst;

            // Aufruf der Load-Methode für jede einzelne ExpenditureList,
            // die geladen werden soll
            for (int i = 0; i < n; i++) {
                lst = new ExpenditureList();
                if(lst.load(din)) {
                    overview.add(lst);
                }
                else {
                    break;
                }
            }
            din.close();
            fin.close();
        }
        catch (IOException e) {
            Log.e("ExpenditureListsOverview", "Error while Loading List from File");
        }
    }

    /**
     * Ändert eine Liste an einer bestimmten Position zu einer neuen Liste
     * @param lstNew Neue Liste
     * @param indexList Position
     */

    public void changeList(ExpenditureList lstNew, int indexList) {
        // Überprüfung, ob Position existiert
        if (indexList >= 0 && indexList < overview.size()) {
            ExpenditureList lst = overview.get(indexList);
            lst.init(lstNew);
        }
        else {
            Log.e("ExpenditureListsOverview", "Error while Changing List");
        }
    }

    /**
     * Gibt die Liste von einer gewählten Position zurück
     * @param position Position der gewünschten Liste
     * @return Liste an der Position
     */

    public ExpenditureList getList(int position){
        return overview.get(position);
    }

    /**
     * Gibt die gesamte Overview-Liste zurück
     * @return Overview
     */

    public ArrayList<ExpenditureList> getOverview() {
        return overview;
    }

    public int getSize(){
        return overview.size();
    }
}
