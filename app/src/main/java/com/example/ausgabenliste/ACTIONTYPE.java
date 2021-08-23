package com.example.ausgabenliste;

public enum ACTIONTYPE {
    EDIT_DELETE,
    NEW;

    public static ACTIONTYPE getEnum(int level) {
        switch (level) {
            case 0:
                return ACTIONTYPE.EDIT_DELETE;
            case 1:
                return ACTIONTYPE.NEW;
            default:
                return ACTIONTYPE.EDIT_DELETE;
        }
    }
}