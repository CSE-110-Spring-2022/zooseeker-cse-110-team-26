package com.example.zooapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExhibitsItemDao {

    @Insert
    long insert(ExhibitsItem exhibitsItem);

    @Insert
    List<Long> insertAll(List<ExhibitsItem> exhibitsItems);

    @Query("SELECT * FROM `exhibits_items` WHERE `key`=:key")
    ExhibitsItem get(long key);

    @Query("SELECT * FROM `exhibits_items` ORDER by `id`")
    List<ExhibitsItem> getAll();

    @Query("SELECT * FROM `exhibits_items` ORDER by `id`")
    LiveData<List<ExhibitsItem>> getAllLive();

    @Update
    int update(ExhibitsItem exhibitsItem);

    @Delete
    int delete(ExhibitsItem exhibitsItem);

}
