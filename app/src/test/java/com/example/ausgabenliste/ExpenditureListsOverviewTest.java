package com.example.ausgabenliste;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExpenditureListsOverviewTest {

    @Test
    public void addList() {
        ExpenditureListsOverview overview = new ExpenditureListsOverview();
        ExpenditureList list = new ExpenditureList("Liste");

        overview.addList(list);

        assertEquals(list, overview.getList(0));
    }

    @Test
    public void deleteList() {
        ExpenditureListsOverview overview = new ExpenditureListsOverview();
        ExpenditureList list1 = new ExpenditureList("Liste 1");
        ExpenditureList list2 = new ExpenditureList("Liste 2");

        overview.addList(list1);
        overview.addList(list2);
        overview.deleteList(0);

        assertEquals("Liste 2", overview.getList(0).getListName());
    }

    @Test
    public void changeList() {
        ExpenditureListsOverview overview = new ExpenditureListsOverview();
        ExpenditureList list = new ExpenditureList("Liste");
        ExpenditureList listNew = new ExpenditureList("Neue Liste");

        overview.addList(list);
        overview.changeList(listNew, 0);

        assertEquals("Neue Liste", overview.getList(0).getListName());
    }
}