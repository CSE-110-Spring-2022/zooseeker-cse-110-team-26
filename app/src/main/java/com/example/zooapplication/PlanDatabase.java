package com.example.zooapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {PlanListItems.class}, version =1)
public abstract class PlanDatabase extends RoomDatabase{
    private static PlanDatabase singleton = null;

    public abstract PlanListItemDao planListItemDao();

    public synchronized static PlanDatabase getSingleton(Context context){
        if (singleton == null){
            singleton = PlanDatabase.makeDatabase(context);
        }

        return singleton;
    }

    private static PlanDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context,PlanDatabase.class,"todo.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() ->{
                            List<PlanListItems> todos = PlanListItems
                                    .loadJSON(context,"animal_list.json");
                            getSingleton(context).planListItemDao().insertAll(todos);
                        });
                    }
                })
                .build();
    }

}
