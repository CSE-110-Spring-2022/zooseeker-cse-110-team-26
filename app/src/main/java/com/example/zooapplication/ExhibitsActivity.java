package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExhibitsActivity extends AppCompatActivity {
    AutoCompleteTextView autoComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibits);
        ExhibitsItemDao dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();
        List<ExhibitsItem> list = dao.getAll();

        List<String> name = new LinkedList<>();
        Set<String> duplicate = new HashSet<>();
        for(ExhibitsItem i : list){
            for(String s: i.tags){
                if(duplicate.add(s)){
                    name.add(s);
                }
            }
        }
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.select_dialog_item, name);

        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
    }
}