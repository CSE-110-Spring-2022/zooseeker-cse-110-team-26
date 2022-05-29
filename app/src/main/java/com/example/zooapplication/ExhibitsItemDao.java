package com.example.zooapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * File Name: ExhibitsItemDao.java
 * Description: construct a database
 */
@Dao
public interface ExhibitsItemDao {

    //insert a single obejct to our database
    @Insert
    long insert(ExhibitsItem exhibitsItem);
    //insert all object to our database
    @Insert
    List<Long> insertAll(List<ExhibitsItem> exhibitsItems);
    //get specific object from our database
    @Query("SELECT * FROM `exhibits_items` WHERE `key`=:key")
    ExhibitsItem get(long key);
    //get all object from our databse
    @Query("SELECT * FROM `exhibits_items` ORDER by `name` AND `id` != 'add'")
    List<ExhibitsItem> getAll();

    @Query("SELECT * FROM `exhibits_items` WHERE `lat` != null")
    List<ExhibitsItem> getAllWithLatLng();


    @Query("SELECT * FROM `exhibits_items`WHERE `id` = 'add'")
    LiveData<List<ExhibitsItem>> getAllLive();

    @Query("SELECT * FROM `exhibits_items`WHERE `id` = 'add'")
    List<ExhibitsItem> getLive();

    @Update
    int update(ExhibitsItem exhibitsItem);

    @Delete
    int delete(ExhibitsItem exhibitsItem);


}
