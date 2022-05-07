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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * displau the route plan, delagate to Direction class
 */
public class DisplayPlanActivity extends AppCompatActivity {
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
        //load data from json file
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

        List<String> sortUnvisited = new LinkedList<>();
        String copyStart = start;
        sortUnvisited.add(copyStart);
        String endPoint = copyStart;

        for(int i = 0; i < id.size(); i++){
            Log.d("test", id.get(i));
        }

        //1. Select a starting city.
        //2. Find the nearest city to your current one and go there.
        //3. If there are still cities not yet visited, repeat step 2. Else, return to the starting city.
        while(id.size() > 0){
            int distance = Integer.MAX_VALUE;
            int count = 0;
            while(count < id.size()){
                int tempDis = Directions.findDistance(copyStart, id.get(count), g, vertexInfo, edgeInfo);
                //Log.d("test", String.valueOf(tempDis));
                if(tempDis < distance) {
                    endPoint = id.get(count);
                    //Log.d("test", "Reassigned to " + String.valueOf(endPoint));
                    distance = tempDis;
                }
                count++;
            }
            sortUnvisited.add(endPoint);
            //Log.d("test", String.valueOf(endPoint));
            id.remove(endPoint);
            copyStart = endPoint;
            //Log.d("test", String.valueOf(endPoint));
        }

        //find the path according to the sortVisited list
        copyStart = start;
        for(String s: sortUnvisited){
            plan.add(Directions.findPath(copyStart,s,g,vertexInfo,edgeInfo));
            copyStart = s;
        }


        ListView view1 = findViewById(R.id.planlist);
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, plan);
        view1.setAdapter(displayAdapter);
    }
}