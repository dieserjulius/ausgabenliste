package com.example.ausgabenliste;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Entry implements Serializable {
    private String entryName;
    private double amount;

    public static final String ENT = "ENTRY";

    public Entry() {}

    /**
     * Konstruktor für einen Eintrag der Liste
     * @param entryName Name des Eintrages
     * @param amount Betrag des Eintrages
     */

    public Entry(String entryName, double amount) {
        this.entryName = entryName;
        this.amount = amount;
    }

    /**
     * Methode zum Speichern des Entrys
     * @param dout DatOutputStream, mit welchem gespeichert werden soll
     * @return Wahrheitswert, ob das Speichern geklappt hat
     */

    public boolean save(DataOutputStream dout) {
        // Wahrheitswert, ob Speichern erfolgreich war
        boolean success = false;
        try {
            dout.writeUTF(entryName);
            dout.writeDouble(amount);
            success = true;
        } catch (IOException e) {
            Log.e("Entry", "Error in save");
        }
        return success;
    }

    /**
     * Methode zum Laden des Entrys
     * @param din DataInputStream, mit welchem geladen werden soll
     * @return Wahrheitswert, ob das Laden geklappt hat
     */

    public boolean load(DataInputStream din) {
        // Wahrheitswert, ob Laden erfolgreich war
        boolean success = false;
        try {
            entryName = din.readUTF();
            amount = din.readDouble();
            success = true;
        } catch (IOException e) {
            Log.e("ExpenditureList", "Error in load");
        }
        return success;
    }

    /**
     * Getter für den Namen des Eintrags
     * @return Name des Eintrags
     */

    public String getEntryName() {
        return entryName;
    }

    /**
     * Getter für den Wert des Eintrags
     * @return Wert des Eintrags
     */

    public double getAmount() {
        return amount;
    }

    /**
     * Gibt den Wert als String wieder
     * @param inEuro Wahrheitswert, ob der Wert des Eintrages mit einem
     *               Eurozeichen zurückgegeben werden soll
     * @return Wert des Eintrags als String
     */

    public String getAmountAsString(boolean inEuro){
        if (inEuro){
            return String.format("%,.2f", amount) +  "€";
        }
        return String.format("%,.2f", amount);
    }

    /**
     * Initialisiert einen neuen Entry
     * @param entNew Entry, der initiaslisiert werden soll
     */

    public void init(Entry entNew) {
        this.entryName = entNew.entryName;
        this.amount = entNew.amount;
    }
}
