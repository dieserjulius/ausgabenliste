package com.example.ausgabenliste;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class ExpenditureList implements Serializable {

    private String listName;

    public static final String LST = "LISTE";

    public ExpenditureList(){}

    public ExpenditureList(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }

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

    public void init(ExpenditureList lstNew) {
        this.listName = lstNew.listName;
    }
}
