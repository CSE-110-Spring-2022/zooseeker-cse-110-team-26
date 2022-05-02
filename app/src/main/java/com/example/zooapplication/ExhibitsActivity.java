package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;

import java.time.chrono.JapaneseChronology;
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

        // get singleton from database
        ExhibitsItemDao dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();
        //get all elements from database
        List<ExhibitsItem> list = dao.getAll();

        //Need to add all the 'tags' into list
        //because user will search for 'id'
        List<String> name = new LinkedList<>();
        //keep track of duplicate element in the dropdown list
        Set<String> duplicate = new HashSet<>();

        //add all strings in the tags.
        for(ExhibitsItem i : list){
            for(String s: i.tags){
                if(duplicate.add(s)){
                    name.add(s);
                }
            }
        }
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);

        ExhibitsItemAdapter adapter = new ExhibitsItemAdapter(this, list);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);

    }

    public void onPlanClicked(View view) {
        Intent intent = new Intent(this,DisplayPlanActivity.class);

        startActivity(intent);
    }
}