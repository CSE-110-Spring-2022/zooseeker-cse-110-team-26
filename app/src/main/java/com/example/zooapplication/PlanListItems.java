package com.example.zooapplication;

import android.content.Context;
import android.media.MediaParser;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Entity(tableName = "plan_list_items")
public class PlanListItems {

    //1. public fields
    @PrimaryKey(autoGenerate = true)
    public long id = 0;

    @NonNull
    public String text;
    public boolean completed;
    public int order;


    //2.constructor matching fields above
    PlanListItems(@NonNull String text,boolean completed, int order){
        this.text = text;
        this.completed = completed;
        this.order = order;
    }

    //3.Factory method loading our JSON
    public static List<PlanListItems> loadJSON(Context context, String path){
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<PlanListItems>>(){}.getType();

            return gson.fromJson(reader,type);
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "PlanListItems{" +
                "text='" + text + '\'' +
                ", completed=" + completed +
                ", order=" + order +
                '}';
    }
}
