package com.example.zooapplication;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExhibitsItemViewModel extends AndroidViewModel {
    private LiveData<List<ExhibitsItem>> exhibitsItems;
    private final ExhibitsItemDao exhibitsItemDao;
    public ExhibitsItemViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitsDatabase db = ExhibitsDatabase.getSingleton(context);
        exhibitsItemDao = db.exhibitsItemDao();
    }

    public LiveData<List<ExhibitsItem>>  getExhibitsItems(){
        if(exhibitsItems == null)
            loadUsers();
        return exhibitsItems;
    }

    private void loadUsers(){
        exhibitsItems = exhibitsItemDao.getAllLive();
    }


    public void deleteItem(ExhibitsItem item){exhibitsItemDao.delete(item);}

}
