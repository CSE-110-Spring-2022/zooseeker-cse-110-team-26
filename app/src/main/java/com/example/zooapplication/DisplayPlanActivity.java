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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * displau the route plan, delagate to Direction class
 */
public class DisplayPlanActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    private ArrayList<String> unvisited;
    private Gson gson;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    Graph g;
    private List<String> plan;
    Directions directions;
    List<String> id;
    private final String  start = "entrance_exit_gate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vertexInfo = ZooData.loadVertexInfoJSON("sample_node_info.json", DisplayPlanActivity.this);
                edgeInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json", DisplayPlanActivity.this);
                g = ZooData.loadZooGraphJSON("sample_zoo_graph.json", DisplayPlanActivity.this);
            }
        });

        plan = new ArrayList<String>();
        gson = new Gson();
        id = new LinkedList<>();
        //the list that contains all the elements that users have typed
        String str = getIntent().getStringExtra("names");
        unvisited = gson.fromJson(str, ArrayList.class);
        //the list that contains all the id that correspond to the clicked items
        String unvisitedId = getIntent().getStringExtra("id");
        id = gson.fromJson(unvisitedId, ArrayList.class);

        Log.d("id", String.valueOf(id.size()));
        plan = directions.printDirections(start, id, g, vertexInfo, edgeInfo);
        Log.d("plan", String.valueOf(plan));

        ListView view1 = findViewById(R.id.planlist);
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, plan);
        view1.setAdapter(displayAdapter);
    }
}