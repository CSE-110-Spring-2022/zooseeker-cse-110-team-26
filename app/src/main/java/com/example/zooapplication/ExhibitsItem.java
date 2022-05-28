/**
 * This file holds an item for each exhibit object
 *
 */
package com.example.zooapplication;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.BuiltInTypeConverters;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * This class is for initializing object.
 * every object contains the animal's type, tag, and id
 */
@Entity(tableName = "exhibits_items")
public class ExhibitsItem {

    @PrimaryKey(autoGenerate = true)
    public long key;
    @NonNull
    public String id;
    public String kind;
    public String name;
    // Database cannot store object, we need to convert it to a string.
    @TypeConverters
    public List<String> tags;
    public String group_id;
    public double lat;
    public double lng;
    //Constructor
    public ExhibitsItem(@NonNull String id, String kind, List<String> tags, String name, String group_id, double lat, double lng){
        this.id = id;
        this.kind = kind;
        this.tags = tags;
        this.name = name;
        this.group_id = group_id;
        this.lng = lng;
        this.lat = lat;

    }

    public String getId(){
        return this.id;
    }
    //get content from .json
    public static List<ExhibitsItem> loadJSON(Context context, String path){
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<ExhibitsItem>>(){}.getType();
            return gson.fromJson(reader, type);
        }catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    @Override
    public String toString() {
        return "ExhibitsItem{" +
                "key=" + key +
                ", id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
