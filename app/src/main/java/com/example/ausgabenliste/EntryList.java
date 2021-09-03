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

public class EntryList {

    /*public EntryList(String listname) {
        listname = listname.toLowerCase();
        listname = listname.replaceAll("\\s+","");
        filename += listname + ".bin";
    }*/

    private String getFilename(String listname) {
        listname = listname.toLowerCase();
        listname = listname.replaceAll("\\s+","");
        String filename = "entry_" + listname + ".bin";
        return filename;
    }

    public ArrayList<Entry> list = new ArrayList<>();

    private static EntryList instance = new EntryList();

    public static EntryList getInstance(){
        return instance;
    }

    public void addEntry(Entry ent){
        list.add(ent);
    }

    public void deleteEntry(int position){
        // Überprüfung, ob Position existiert
        if (position >= 0 && position < list.size()) {
            list.remove(position);
        }
        else {
            Log.e("EntryList", "Position of List out of Bounds");
        }
    }

    public void changeEntry(Entry entNew, int indexList) {
        // Überprüfung, ob Position existiert
        if (indexList >= 0 && indexList < list.size()) {
            Entry ent = list.get(indexList);
            ent.init(entNew);
        }
        else {
            Log.e("EntryList", "Error while Changing List");
        }
    }

    public void saveInput(Context context, String listname) {
        try {
            // Vorbereitung
            String filename = getFilename(listname);
            FileOutputStream fout = context.openFileOutput(filename, MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.writeInt(list.size());
            Log.i("EntryList", "Saving List to File ok");

            // Aufruf der Save-Methode für jede einzelne ExpenditureList
            for (Entry ent : list) {
                ent.save(dout);
            }

            dout.close();
            fout.close();
        }
        catch (IOException e) {
            Log.e("EntryList", "saveError in saveInput");
        }
    }

    public void loadInput(Context context, String listname) {
        try {
            // Vorbereitung
            list.clear();
            String filename = getFilename(listname);
            FileInputStream fin = context.openFileInput(filename);
            DataInputStream din = new DataInputStream(fin);
            int n = din.readInt();
            Log.i("EntryList", "Loading List from File ok");
            Entry ent;

            // Aufruf der Load-Methode für jede einzelne ExpenditureList,
            // die geladen werden soll
            for (int i = 0; i < n; i++) {
                ent = new Entry();
                if(ent.load(din)) {
                    list.add(ent);
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

    public Entry getEntry(int position){
        return list.get(position);
    }

    public ArrayList<Entry> getList() {
        return list;
    }
}
