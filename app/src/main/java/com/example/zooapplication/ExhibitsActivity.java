package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExhibitsActivity extends AppCompatActivity {
    AutoCompleteTextView autoComplete;
    ArrayList<String> result;
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
            if(i.kind.equals("exhibit")){
                for(String s: i.tags){
                    if(duplicate.add(s)){
                        name.add(s);
                    }
                }
            }
        }
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);
        //contains what users have clicked
        result = new ArrayList<>();
        ExhibitsItemAdapter adapter = new ExhibitsItemAdapter(this, name);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
        ListView view1 = findViewById(R.id.dis);
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        view1.setAdapter(displayAdapter);
//        Button button = findViewById(R.id.button);
//        TextView view2 = findViewById(R.id.exhibits_items);
//        DisplayAdapter displayAdapter = new DisplayAdapter();
//        displayAdapter.setHasStableIds(true);
//        recyclerView = findViewById(R.id.recycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(displayAdapter);
//        displayAdapter.setStringList(name);

//        if(button != null){
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d("test", "yes");
//                    result.add(view2.getText().toString());
//                    for(String s:result){
//                        Log.d("test", s);
//                    }
//                    view1.setText(view2.getText().toString());
//                }
//            });
//        }
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                result.add(0, adapterView.getItemAtPosition(i).toString());
                displayAdapter.notifyDataSetChanged();
                //displayAdapter.setStringList(new ArrayList<>(result));
                //view1.setText(adapterView.getItemAtPosition(i).toString());
            }
        });
    }

    public void onPlanClicked(View view) {
        Intent intent = new Intent(this,DisplayPlanActivity.class);
        intent.putStringArrayListExtra("result", result);
        startActivity(intent);
    }
}