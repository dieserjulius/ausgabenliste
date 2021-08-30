package com.example.ausgabenliste;

public class Entry {
    private String entryName;
    private double amount;

    /**
     * Konstruktor für einen Eintrag der Liste
     * @param entryName Name des Eintrages
     * @param amount Betrag des Eintrages
     */

    public Entry(String entryName, double amount) {
        this.entryName = entryName;
        this.amount = amount;
    }
}
