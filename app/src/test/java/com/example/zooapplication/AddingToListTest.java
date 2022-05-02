package com.example.zooapplication;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;

import android.content.Context;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class AddingToListTest{
    private PlanListItemDao dao;
    private PlanDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, PlanDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.planListItemDao();
    }

    @After
    public void closeDb() throws IOException{
        db.close();
    }


    @Test
    public void testInsert(){
        PlanListItems item1 = new PlanListItems("Pizza time", false, 0);
        PlanListItems item2 = new PlanListItems("Photos of Spider-Man", false, 1);

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item1);

        String s = String.valueOf(id1);
        System.out.println("hello my name is eliel");
        System.out.printf("%d%n",id2);

        assertNotEquals(id1,id2);
    }

    @Test
    public void testSizeOfList(){

        long id1 = 0;
        for(int i = 0; i < 5; i++){
            PlanListItems item1 = new PlanListItems("Pizza time", false, 0);
            id1 = dao.insert(item1);
        }
        long five = 5;
        assertEquals(five,id1);

    }



}