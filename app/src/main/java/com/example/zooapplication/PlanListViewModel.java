package com.example.zooapplication;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlanListViewModel extends AndroidViewModel {

    private LiveData<List<PlanListItems>> todoListItems;
    private final PlanListItemDao todoListItemDao;

    public PlanListViewModel(@NonNull Application application){
        super(application);
        Context context = getApplication().getApplicationContext();
        PlanDatabase db = PlanDatabase.getSingleton(context);
        todoListItemDao = db.planListItemDao();
    }


    public LiveData<List<PlanListItems>> getTodoListItems(){
        if(todoListItems == null){
            loadUsers();
        }

        return todoListItems;
    }

    private void loadUsers(){
        todoListItems = todoListItemDao.getAllLive();
    }

    public void createTodo(String text){
        int endOfListOrder = todoListItemDao.getOrderForAppend();
        PlanListItems newItem = new PlanListItems(text,false,endOfListOrder);
        todoListItemDao.insert(newItem);
    }

}
