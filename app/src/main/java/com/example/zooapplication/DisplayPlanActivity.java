package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DisplayPlanActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private ArrayList<String> display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);
        display = getIntent().getStringArrayListExtra("result");

//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display_plan);
//
//
//        PlanListAdapter adapter = new PlanListAdapter();
//        adapter.setHasStableIds(true);
//
//        recyclerView = findViewById(R.id.plan_items);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//
//        adapter.setPlanListItems(PlanListItems.loadJSON(this,"animal_list.json"));


    }
}