package com.example.zooapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * local database
 * get all value from the .json file and
 * load into local database
 * Similar to Lab 5
 */
@Database(entities = {ExhibitsItem.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ExhibitsDatabase extends RoomDatabase {
    private static ExhibitsDatabase singleton = null;

    public abstract ExhibitsItemDao exhibitsItemDao();

    public synchronized static ExhibitsDatabase getSingleton(Context context){
        if(singleton == null){
            singleton = ExhibitsDatabase.makeDatabase(context);
        }
        return singleton;
    }
    private static ExhibitsDatabase makeDatabase(Context context){
        return Room.databaseBuilder(context, ExhibitsDatabase.class, "sample_exhibits_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() ->{
                            List<ExhibitsItem> exhibitsItems = ExhibitsItem.loadJSON(context, "sample_exhibits.json");
                            getSingleton(context).exhibitsItemDao().insertAll(exhibitsItems);
                        });
                    }
                })
                .build();
    }
}
