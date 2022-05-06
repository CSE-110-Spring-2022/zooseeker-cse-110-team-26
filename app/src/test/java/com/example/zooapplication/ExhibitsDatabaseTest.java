package com.example.zooapplication;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Database test work properly
 */
@RunWith(AndroidJUnit4.class)
public class    ExhibitsDatabaseTest {
    private ExhibitsItemDao dao;
    private ExhibitsDatabase db;


    //create database
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ExhibitsDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.exhibitsItemDao();
    }

    //close database
    @After
    public void closeDb() throws IOException {
        db.close();
    }
    //test insert
    @Test
    public void testInsert(){
        List<String> list1 = new LinkedList<String>();
        list1.add("enter");
        list1.add("start");
        list1.add("begin");
        ExhibitsItem item1 = new ExhibitsItem("entrance-exit-gate-1", "gate", list1);
        long id = dao.insert(item1);
        List<String> list2 = new LinkedList<String>();
        list2.add("gorilla");
        list2.add("monkey");
        list2.add("ape");
        list2.add("mammal");
        ExhibitsItem item2 = new ExhibitsItem("gorilla-viewpoint-1", "exhibit", list2);
        long id2 = dao.insert(item2);
        assertNotEquals(id,id2);

    }
    @Test
    public void testGet() {
        List<String> list1 = new LinkedList<String>();
        list1.add("enter");
        list1.add("start");
        list1.add("begin");
        ExhibitsItem item1 = new ExhibitsItem("entrance-exit-gate-1", "gate", list1);
        long key = dao.insert(item1);

        ExhibitsItem item2 = dao.get(key);
        assertEquals(key, item2.key);
        assertEquals(item1.id, item2.id);
        assertEquals(item1.tags, item2.tags);
        assertEquals(item1.itemType, item2.itemType);
    }

    @Test
    public void testUpdate(){
        List<String> list1 = new LinkedList<String>();
        list1.add("enter");
        list1.add("start");
        list1.add("begin");
        ExhibitsItem item1 = new ExhibitsItem("entrance-exit-gate-1", "gate", list1);
        long key = dao.insert(item1);

        item1 = dao.get(key);
        item1.id = "test";
        int itemsUpdated = dao.update(item1);
        assertEquals(1, itemsUpdated);

        item1 = dao.get(key);
        assertEquals("test", item1.id);
    }

    @Test
    public void testDelete() {
        List<String> list1 = new LinkedList<String>();
        list1.add("enter");
        list1.add("start");
        list1.add("begin");
        ExhibitsItem item1 = new ExhibitsItem("entrance-exit-gate-1", "gate", list1);
        long key = dao.insert(item1);

        item1 = dao.get(key);
        int item1Deleted = dao.delete(item1);
        assertEquals(1, item1Deleted);
        assertNull(dao.get(key));
    }

    @Test
    public void testGetAll(){
        List<String> list1 = new LinkedList<String>();
        list1.add("enter");
        list1.add("start");
        list1.add("begin");
        ExhibitsItem item1 = new ExhibitsItem("entrance-exit-gate-1", "gate", list1);
        long key = dao.insert(item1);

        List<String> list2 = new LinkedList<String>();
        list2.add("gorilla");
        list2.add("monkey");
        list2.add("ape");
        list2.add("mammal");
        ExhibitsItem item2 = new ExhibitsItem("gorilla-viewpoint-1", "exhibit", list2);
        long id2 = dao.insert(item2);
        List<ExhibitsItem> ex = new LinkedList<>();
        ex.add(item1);
        ex.add(item2);
        long key1 = dao.insert(item1);
        long key2 = dao.insert(item2);
        List<ExhibitsItem> ori = new LinkedList<>();
        ori.add(item1);
        ori.add(item2);
        List<ExhibitsItem> result = dao.getAll();
        assertEquals(2, ori.size());
    }
}
