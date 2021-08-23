package com.example.ausgabenliste;

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
}
