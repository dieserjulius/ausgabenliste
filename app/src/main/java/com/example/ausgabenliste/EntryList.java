package com.example.ausgabenliste;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class EntryList implements Serializable {

    /**
     * Erstellt aus einem Listname einen geeigneten Dateinamen
     * @param listname Name der Liste
     * @return Name für die Datei
     */

    private String getFilename(String listname) {
        // Kreiert Dateinamen, der gut zum einlesen ist (nur LowerCase und keine Leerzeichen)
        listname = listname.toLowerCase();
        listname = listname.replaceAll("\\s+","");
        String filename = "entry_" + listname + ".bin";
        return filename;
    }

    public ArrayList<Entry> list = new ArrayList<>();

    private static EntryList instance;

    /**
     * Erstellt eine neue Instanz und gibt diese zurück
     * @return Neue Instanz
     */

    public static EntryList init(){
        instance = new EntryList();
        return instance;
    }

    /**
     * Getter für die Instanz
     * @return Instanz
     */

    public static EntryList getInstance(){
        return instance;
    }

    /**
     * Fügt einen Entry der List hinzu
     * @param ent Entry, der hinzugefügt werden soll
     */

    public void addEntry(Entry ent){
        list.add(ent);
    }

    /**
     * Löscht einen Entry aus der List
     * @param position Position des Entry, welcher gelöscht werden soll
     */

    public void deleteEntry(int position){
        // Überprüfung, ob Position existiert
        if (position >= 0 && position < list.size()) {
            list.remove(position);
        }
        else {
            Log.e("EntryList", "Position of List out of Bounds");
        }
    }

    /**
     * Ändert einen Entry an einer bestimmten Position zu einem neuen Entry
     * @param entNew neuer Entry
     * @param indexList Position
     */

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

    /**
     * Methode zum Speichern
     * @param context
     * @param listname Name der Liste, in der der Entry ist
     */

    public void saveInput(Context context, String listname) {
        try {
            // Vorbereitung
            String filename = getFilename(listname);
            FileOutputStream fout = context.openFileOutput(filename, MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.writeInt(list.size());
            Log.i("EntryList", "Saving List to File ok");

            // Aufruf der Save-Methode für jeden einzelnen Entry
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

    /**
     * Methode zum Speichern
     * @param context
     * @param listname Name der Liste, in der der Entry ist
     */

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

            // Aufruf der Load-Methode für jeden einzelnen Entry,
            // der geladen werden soll
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
            Log.e("EntryList", "Error while Loading List from File:\n" + e);
        }
    }

    /**
     * Löscht die Datei einer Liste
     * @param context
     * @param listname Liste, dessen Datei gelöscht werden soll
     * @return Wahrheitswert, ob Löschen geklappt hat.
     */

    public boolean deleteFile(Context context, String listname){
        String filename = getFilename(listname);
        File dir = context.getFilesDir();
        File file = new File(dir, filename);
        return file.delete();
    }

    /**
     * Überträgt die Daten einer Datei in eine neue und löscht die alte
     * @param context
     * @param oldListname Alter Name der Liste
     * @param newListname Neuer Name der Liste
     */

    public void renameFile (Context context, String oldListname, String newListname){
        // Durch speichern unter neuem Namen und Löschen der alten Datei wird
        // die alte Datei ersetzt
        saveInput(context, newListname);
        deleteFile(context, oldListname);
    }

    /**
     * Getter für ein Entry an einer bestimmten Position
     * @param position Gewünschte Position
     * @return Entry an der gewünschten Position
     */

    public Entry getEntry(int position){
        return list.get(position);
    }

    /**
     * Getter für die Liste
     * @return Aktuelle Liste
     */

    public ArrayList<Entry> getList() {
        return list;
    }
}
