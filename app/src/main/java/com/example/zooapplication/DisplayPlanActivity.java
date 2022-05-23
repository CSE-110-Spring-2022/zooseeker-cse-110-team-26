/**
 * This file displays the route plan, prints the list of animals in sorted order, and sends the
 * plan to the DirectionsActivity class to display instructions
 */
package com.example.zooapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private final String start = "entrance_exit_gate";
    private Button directionButton;
    private Button goBack;
    String copyStart = start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_plan);
        plan = new ArrayList<String>();
        gson = new Gson();
        id = new ArrayList<>();
        unvisited = new ArrayList<>();

        //All infomation I need to use in this activity are store in Preferences.
        //So we need to get info from that class, and also, that claas returns a string
        //we need to use gson to convert back the original structure
        String unvisitedName = ShareData.getResultName(App.getContext(), "result name");
        String unvisitedId = ShareData.getResultId(App.getContext(), "result id");
        Gson gson = new Gson();
        unvisited = gson.fromJson(unvisitedName, ArrayList.class);
        id = gson.fromJson(unvisitedId, ArrayList.class);

        //Connect to UI views
        goBack = findViewById(R.id.go_back);
        directionButton = findViewById(R.id.direction);
        //load data from json file
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vertexInfo = ZooData.loadVertexInfoJSON("sample_node_info.json",
                        DisplayPlanActivity.this);
                edgeInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json",
                        DisplayPlanActivity.this);
                g = ZooData.loadZooGraphJSON("sample_zoo_graph.json",
                        DisplayPlanActivity.this);

            }
        });


        plan = new ArrayList<String>();
        gson = new Gson();
        //the list that contains all the elements that users have typed
        String str = getIntent().getStringExtra("names");
        unvisited = gson.fromJson(str, ArrayList.class);
        //the list that contains all the id that correspond to the clicked items
        id = gson.fromJson(unvisitedId, ArrayList.class);



        /*
        sortUnvisited.add(copyStart);

        String endPoint = copyStart;

        //Creates the route and puts the order in which we visit the animals into sortUnvisited list
        createRoute(sortUnvisited, copyStart, endPoint);

        //find the path according to the sortVisited list
        copyStart = start;
        for(String s: sortUnvisited){
            plan.add(Directions.findPath(copyStart, s, g, vertexInfo, edgeInfo));
            copyStart = s;
        }
        display(sortUnvisited);


        //Button that transfers you to the DirectionsActivity class
        directionClicked();
        //Button that transfers you to the ExhibitsActivity
        goBack();
        shareData();
    }
         */
    }
    /**
     * store this activity as the last activity in case user kills the app in this activity
     */
    private void shareData () {
        ShareData.setLastActivity(App.getContext(), "last activity", getClass().getName());
    }

        /**
         * Give a detail description base on the optimal route plan
         * @param sortUnvisited the optimal route plan
         * @param copyStart Starting point
         * @param endPoint next point
         */
        private void createRoute (List < String > sortUnvisited, String copyStart, String endPoint){
            while (id.size() > 0) {
                int distance = Integer.MAX_VALUE;
                int count = 0;
                while (count < id.size()) {
                    int tempDis = Directions.findDistance
                            (copyStart, id.get(count), g, vertexInfo, edgeInfo);
                    //Takes smallest distance
                    if (tempDis < distance) {
                        endPoint = id.get(count);
                        distance = tempDis;
                    }
                    count++;
                }
                sortUnvisited.add(endPoint);
                id.remove(endPoint);
                copyStart = endPoint;
            }

        }

        private void display (List < String > sortUnvisited) {


            sortUnvisited = Route.sortExhibits(id, copyStart, g, vertexInfo, edgeInfo);
            //find the path according to the sortVisited list
            copyStart = start;
//        for(String s: sortUnvisited){
//            plan.add(Directions.findPath(copyStart, s, g, vertexInfo, edgeInfo));
//            copyStart = s;
//        }

            plan = Route.createRoute(sortUnvisited, copyStart, g, vertexInfo, edgeInfo);

            List<String> displayPlan = new LinkedList<>(sortUnvisited);
            displayPlan.remove(0);
            ListView view1 = findViewById(R.id.planlist);
            ArrayAdapter displayAdapter = new ArrayAdapter
                    (this, android.R.layout.simple_list_item_1, displayPlan);
            view1.setAdapter(displayAdapter);
        }

        private void directionClicked () {
            directionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    //Passes the plan to DirectionsActivity
                    String dire = gson.toJson(plan);
                    Intent intent = new Intent
                            (DisplayPlanActivity.this, DirectionsActivity.class);


                    intent.putExtra("names", dire);
                    String ids = gson.toJson(sortUnvisited);
                    intent.putExtra("id", ids);
                    startActivity(intent);
                }
            });
        }

        private void goBack () {
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