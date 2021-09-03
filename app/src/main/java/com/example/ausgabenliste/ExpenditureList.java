package com.example.ausgabenliste;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ExpenditureList implements Serializable {

    private String listName;
    private EntryList entryList;

    public static final String LST = "LISTE";

    /**
     * Leerer Konstruktor
     */

    public ExpenditureList(){}

    /**
     * Konstruktor
     * @param listName Gewünschter Name der Liste
     */

    public ExpenditureList(String listName) {
        this.listName = listName;
        // Liste mit Einträgen folgt noch, da Programm noch nicht fertig
    }

    /**
     * Getter für den Namen der Liste
     * @return Name der Liste
     */

    public String getListName() {
        return listName;
    }

    /**
     * Methode zum Speichern der Liste
     * @param dout DatOutputStream, mit welchem gespeichert werden soll
     * @return Wahrheitswert, ob das Speichern geklappt hat
     */

    public boolean save(DataOutputStream dout) {
        boolean success = false;
        try {
            dout.writeUTF(listName);
            success = true;
        } catch (IOException e) {
            Log.e("ExpenditureList", "Error in save");
        }
        return success;
    }

    /**
     * Methode zum Laden der Liste
     * @param din DataInputStream, mit welchem geladen werden soll
     * @return Wahrheitswert, ob das Laden geklappt hat
     */

    public boolean load(DataInputStream din) {
        boolean success = false;
        try {
            listName = din.readUTF();
            success = true;
        } catch (IOException e) {
            Log.e("ExpenditureList", "Error in load");
        }
        return success;
    }

    /**
     * Initilasieren der Liste
     * @param lstNew Neue Liste
     */

    public void init(ExpenditureList lstNew) {
        this.listName = lstNew.listName;
    }
}
