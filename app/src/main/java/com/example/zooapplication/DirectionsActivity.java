/**
 * This file contains the the methods and UI for the screens that display the directions
 */
package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DirectionsActivity extends AppCompatActivity {
    private List<String> directions;
    int count = 1;
    TextView displayDirection;
    Button getNextDirection;
    Button skipDirection;
    Button goBack;
    private final String start = "entrance_exit_gate";
    List<String> id;
    private Gson gson;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    Graph g;
    Iterator<String> it = null;
    String copyStart = start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        //Connects to UI
        displayDirection = findViewById(R.id.currentDirection);
        getNextDirection = findViewById(R.id.getNextDirection);
        skipDirection = findViewById(R.id.skip);
        goBack = findViewById(R.id.back);

        //load data from json file
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vertexInfo = ZooData.loadVertexInfoJSON("sample_node_info.json",
                        DirectionsActivity.this);
                edgeInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json",
                        DirectionsActivity.this);
                g = ZooData.loadZooGraphJSON("sample_zoo_graph.json",
                        DirectionsActivity.this);

            }
        });
        //Inputs the list of instructions passed in from DisplayPlayActivity
        Gson gson = new Gson();
        //Get the list of directions
        String str = getIntent().getStringExtra("names");
        directions = gson.fromJson(str, List.class);
        //the list that contains all the id that correspond to the clicked items
        String unvisitedId = getIntent().getStringExtra("id");
        id = gson.fromJson(unvisitedId, ArrayList.class);
        //Removes entrance exit gate
        id.remove(0);
        //Removes first exhibit because we are already there
        copyStart = id.get(0);
        id.remove(0);
        /*for(int i = 0; i < id.size(); i++){
            Log.d("test", id.get(i));
        }
        for(int i = 0; i < id.size(); i++){
            Log.d("test", directions.get(i));
        }*/
        //Connects the plan with the UI
        displayDirection.setText(directions.get(1));
        //Updates the Textview UI if the Next button is clicked
        getNextDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count >= directions.size()){
                    Utilities.showAlert(DirectionsActivity.this,
                            "The route is done!");

                }
                else{
                    //Log.d("direction", String.valueOf(directions.get(count)));
                    displayDirection.setText(directions.get(count));
                    copyStart = id.get(0);
                    id.remove(0);

                }
            }
        });
        //If the goBack button is clicked, exit DirectionsAcitivty class
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        skipDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(id.size() == 0){
                    Utilities.showAlert(DirectionsActivity.this,
                            "The route is finished! Nothing to skip.");
                }
                else if(id.size() == 1){
                    Utilities.showAlert(DirectionsActivity.this,
                            "Only 1 exhibit left! Unable to skip.");
                }
                else {
                    id.remove(0);
                    id = Route.sortExhibits(id, copyStart, g, vertexInfo, edgeInfo);
                    directions = Route.createRoute(id, copyStart, g, vertexInfo, edgeInfo);
                    count = 1;
                    id.remove(0);
                    displayDirection.setText(directions.get(count));
                    for (int i = 0; i < id.size(); i++) {
                        Log.d("hi", id.get(i));
                    }
                    for (int i = 0; i < id.size(); i++) {
                        Log.d("hi", directions.get(i));
                    }
                    copyStart = id.get(0);
                    id.remove(0);
                }
            }
        });
    }
}