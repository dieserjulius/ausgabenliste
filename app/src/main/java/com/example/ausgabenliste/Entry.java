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

    public boolean save(DataOutputStream dout) {
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

    public boolean load(DataInputStream din) {
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

    public String getEntryName() {
        return entryName;
    }

    public double getAmount() {
        return amount;
    }

    public String getAmountAsString(boolean inEuro){
        if (inEuro){
            return String.format("%,.2f", amount) +  "€";
        }
        return String.format("%,.2f", amount);
    }

    public void init(Entry entNew) {
        this.entryName = entNew.entryName;
        this.amount = entNew.amount;
    }
}
