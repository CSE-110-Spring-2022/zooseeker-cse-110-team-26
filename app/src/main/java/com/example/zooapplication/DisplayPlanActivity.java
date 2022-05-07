package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        for(int i = 0; i < display.size();i++){
            System.out.println(display.get(i)+"\n");
        }

        ListView view1 = findViewById(R.id.planlist);
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, display);
        view1.setAdapter(displayAdapter);




    }
}