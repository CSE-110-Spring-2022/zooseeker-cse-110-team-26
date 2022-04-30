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
 * This class is for initilizing object.
 * every object contains the animal's type, tag, and id
 */
@Entity(tableName = "exhibits_items")
public class ExhibitsItem {

    @PrimaryKey(autoGenerate = true)
    public long key;
    @NonNull
    public String id;
    public String itemType;

    // Database cannot store object, we need to convert it to a string.
    @TypeConverters
    public List<String> tags;

    //Constructor
    public ExhibitsItem(@NonNull String id, String itemType, List<String> tags){
        this.id = id;
        this.itemType = itemType;
        this.tags = tags;
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
                "id='" + id + '\'' +
                ", type='" + itemType + '\'' +
                ", tags=" + tags +
                '}';
    }
}
