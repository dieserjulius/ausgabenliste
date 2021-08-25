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

public class ExpenditureListsOverview {

    private final String FILENAME = "expendditurelists.bin";

    public ArrayList<ExpenditureList> overview = new ArrayList<>();

    private static ExpenditureListsOverview instance = new ExpenditureListsOverview();

    public ExpenditureListsOverview() {
    }

    public static ExpenditureListsOverview getInstance(){
        return instance;
    }

    public void addList(ExpenditureList lst){
        overview.add(lst);
    }

    public void saveInput(Context context) {
        try {
            FileOutputStream fout = context.openFileOutput(FILENAME, MODE_PRIVATE);
            DataOutputStream dout = new DataOutputStream(fout);
            dout.writeInt(overview.size());
            Log.i("ExpenditureListsOverview", "savetoFile, openfile, size: "+overview.size());
            for (ExpenditureList lst : overview) {
                lst.save(dout);
            }

            dout.close();
            fout.close();
        }
        catch (IOException e) {
            Log.e("ExpenditureListsOverview", "save2File, Error in save2File");
        }
    }

    public void loadInput(Context context) {
        try {
            overview.clear();
            FileInputStream fin = context.openFileInput(FILENAME);
            DataInputStream din = new DataInputStream(fin);
            int n = din.readInt();
            Log.i("ExpenditureListsOverview", "Loading List from File ok");
            ExpenditureList lst;
            for (int i=0; i<n; i++) {
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

    public void changeList(ExpenditureList lstNew, int indexList) {
        if (indexList<=0 && indexList<overview.size()) {
            ExpenditureList lst = overview.get(indexList);
            lst.init(lstNew);
        }
        else {
            Log.e("ExpenditureListsOverview", "Error while Changing List");
        }
    }

    public ArrayList<ExpenditureList> getOverview() {
        return overview;
    }
}
