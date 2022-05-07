package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisplayPlanActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private ArrayList<String> names;
    private Gson gson;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    Graph g;
    private ArrayList<String> plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);


        plan = new ArrayList<String>();
        gson = new Gson();

        String str = getIntent().getStringExtra("names");
        names = gson.fromJson(str, ArrayList.class);

        String graph = getIntent().getStringExtra("graph");
        g = gson.fromJson(graph, Graph.class);

        String vertex = getIntent().getStringExtra("vertexInfo");
        vertexInfo = gson.fromJson(vertex, Map.class);

        String edge = getIntent().getStringExtra("edgeInfo");
        vertexInfo = gson.fromJson(edge, Map.class);

        String start = getIntent().getStringExtra("start");

        while(names.size() > 0){

        }


        ListView view1 = findViewById(R.id.planlist);
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
        view1.setAdapter(displayAdapter);




    }
}