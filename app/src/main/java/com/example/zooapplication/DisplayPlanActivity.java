/**
 * This file displays the route plan, prints the list of animals in sorted order, and sends the
 * plan to the DirectionsActivity class to display instructions
 */
package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DisplayPlanActivity extends AppCompatActivity {
    private ArrayList<String> unvisited;
    private Gson gson;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    Graph g;
    private List<String> plan;
    Directions directions;
    ArrayList<String> id;
    List<String> sortUnvisited;
    private final String  start = "entrance_exit_gate";
    private Button directionButton;
    private Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);
        plan = new ArrayList<String>();
        gson = new Gson();
        //Connect to UI views
        goBack = findViewById(R.id.go_back);
        directionButton = findViewById(R.id.direction);
        //All infomation I need to use in this activity are store in Preferences.
        //So we need to get info from that class, and also, that claas returns a string
        //we need to use gson to convert back the original structure
        String unvisitedName = ShareData.getResultName(App.getContext(), "result name");
        String unvisitedId = ShareData.getResultId(App.getContext(), "result id");
        unvisited = gson.fromJson(unvisitedName, ArrayList.class);
        id = gson.fromJson(unvisitedId, ArrayList.class);
        //load data from json file
        loadZooData();
        String copyStart = start;
        sortUnvisited = Route.sortExhibits(id, copyStart, g, vertexInfo, edgeInfo);
        //find the path according to the sortVisited list
        copyStart = start;
        plan = Route.createRoute(sortUnvisited, copyStart, g, vertexInfo, edgeInfo);
        List<String> displayPlan = new LinkedList<>(sortUnvisited);

        //looking for distance to planned item, and location where planned item is located
        String disStrt = "entrance_exit_gate";
        double dist = 0;
        for(int i = 0; i < displayPlan.size(); i++){
            String id = displayPlan.get(i);

            //Since not all of the planned items are in id form, must normalise
            id = Directions.getID(id,g,vertexInfo,edgeInfo);

            //Getting Street and Distance
            dist = dist + Directions.findDistance(disStrt,id,g,vertexInfo,edgeInfo);
            disStrt = id;
            String street = Directions.findStreet(id,g,vertexInfo,edgeInfo);

            //updating displayPlan list items to include distance and street
            displayPlan.set(i,displayPlan.get(i) + '\n'+ "Distance: " + dist + " ft." + "\n" + "Street: " + street);
        }

        ListView view1 = findViewById(R.id.planlist);
        ArrayAdapter displayAdapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, displayPlan);
        view1.setAdapter(displayAdapter);
        //Button that transfers you to the DirectionsActivity class
        directionButtonClicked();
        goBackClicked();
        ShareData.setLastActivity(App.getContext(),"last activity", getClass().getName());
    }

    /**
     * load zoo data from json file
     */
    private void loadZooData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vertexInfo = ZooData.loadVertexInfoJSON("exhibit_info.json",
                        DisplayPlanActivity.this);
                edgeInfo = ZooData.loadEdgeInfoJSON("trail_info.json",
                        DisplayPlanActivity.this);
                g = ZooData.loadZooGraphJSON("zoo_graph.json",
                        DisplayPlanActivity.this);
            }
        });
    }

    /**
     * direction button is clicked
     * We need to pass the sorted plan list to next
     * activity
     */
    private void directionButtonClicked() {
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                //Passes the plan to DirectionsActivity
                String dire = gson.toJson(plan);
                Intent intent = new Intent
                        (DisplayPlanActivity.this, DirectionsActivity.class);
                ShareData.setNames(App.getContext(),"names", dire);
                String ids = gson.toJson(sortUnvisited);
                ShareData.setIds(App.getContext(),"ids", ids);
                startActivity(intent);
            }
        });
    }

    /**
     * If go back is clicked, the go back to the previou UI
     */
    private void goBackClicked() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPlanActivity.this, ExhibitsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}