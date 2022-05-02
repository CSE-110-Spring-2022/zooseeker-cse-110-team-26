package com.example.zooapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlanListItemDao {
    @Insert
    long insert(PlanListItems todoListItem);

    @Query("SELECT* FROM `plan_list_items` WHERE `id`=:id")
    PlanListItems get(long id);

    @Query("SELECT* FROM `plan_list_items` ORDER BY `order`")
    List<PlanListItems> getAll();

    @Query("SELECT `order` + 1 FROM `plan_list_items` ORDER BY `order` DESC LIMIT 1")
    int getOrderForAppend();

    @Query("SELECT* FROM `plan_list_items` ORDER BY `order`")
    LiveData<List<PlanListItems>> getAllLive();


    @Update
    int update(PlanListItems todoListItem);

    @Delete
    int delete(PlanListItems todoListItem);

    @Insert
    List<Long> insertAll(List<PlanListItems> planListItems);
}
