/**
 * This file contains the the methods and UI for the screens that display the directions
 */
package com.example.zooapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DirectionsActivity extends AppCompatActivity {
    private List<String> directions;
    private ExhibitsItemDao dao;
    int count = 0;
    TextView displayDirection;
    Button getNextDirection;
    Button skipDirection;
    Button goBack;
    Button getStepBack;
    Switch detailed;
    private final String start = "entrance_exit_gate";
    List<String> id;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    Graph g;
    Iterator<String> it = null;
    String copyStart = start;
    Stack<String> stepBack;

    private void setDirections(List<String> directions){
        if(detailed.isChecked()) {
            displayDirection.setText(directions.get(count));
        }
        else {
            displayDirection.setText(DetailedtoBrief.toBrief(directions.get(count)));
        }
    }
    private void setDirections(String directions){
        if(detailed.isChecked()) {
            displayDirection.setText(directions);
        }
        else {
            displayDirection.setText(DetailedtoBrief.toBrief(directions));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        getStepBack = findViewById(R.id.step_back);
        stepBack = new Stack<>();
        Gson gson = new Gson();
        directions = new ArrayList<>();
        String di = ShareData.getNames(App.getContext(), "names");
        directions = gson.fromJson(di, ArrayList.class);
        String i = ShareData.getResultId(App.getContext(), "ids");
        id = gson.fromJson(i, ArrayList.class);
        for(String s: id){
            Log.d("id1234", String.valueOf(s));
        }
        //Connects to UI
        displayDirection = findViewById(R.id.currentDirection);
        getNextDirection = findViewById(R.id.getNextDirection);
        skipDirection = findViewById(R.id.skip);
        goBack = findViewById(R.id.back);
        detailed = findViewById(R.id.setting);

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
        for(String s :id){
            Log.d("Test", String.valueOf(s));
        }
        for(String s : directions){
            Log.d("Test", String.valueOf(s));
        }

        //stepBack.push(id.get(0));
        //Removes entrance exit gate
        //id.remove(0);
        //Removes first exhibit because we are already there
        copyStart = id.get(0);
        id.remove(0);
        stepBack.push(start);
        /*for(int i = 0; i < id.size(); i++){
            Log.d("test", id.get(i));
        }
        */
        //Connects the plan with the UI
        setDirections(directions);
        //Updates the Textview UI if the Next button is clicked
        getNextDirectionClicked();
        //If the goBack button is clicked, exit DirectionsAcitivty class
        goBackClicked();
        shareData();

        getStepBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepBack.empty()){
                    Utilities.showAlert(DirectionsActivity.this, "No previous item!");
                }
                else{
                    String endPoint = stepBack.pop();
                    if(count != directions.size()){
                        id.add(0, copyStart);
                    }
                    count--;
//                    for(String s: id){
//                        Log.d("Test", String.valueOf(s));
//                  }
                    Log.d("stepBack", "Copy Start: " + copyStart);
                    Log.d("stepBack", "EndPoint: " + endPoint);
                    String toPrevious = Directions.findPath(copyStart, endPoint, g, vertexInfo, edgeInfo);
                    displayDirection.setText(toPrevious);
                    copyStart = endPoint;
                }
                Log.d("Count", String.valueOf(count));
            }
        });
    }

    public List<String> getDirections(){
        return this.directions;
    }
    private void shareData(){;
        ShareData.setLastActivity(App.getContext(), "last activity", getClass().getName());
    }
    private void goBackClicked() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayPlanActivity.class);
                //List<String> list = gson.fromJson(s, ArrayList.class);
//               for(String str :list){
//                   Log.d("test", String.valueOf(str));
//               }
                startActivity(intent);
                finish();

            }
        });
    }

    private void getNextDirectionClicked() {
        getNextDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count >= directions.size()){
                    Utilities.showAlert(DirectionsActivity.this,
                            "The route is done!");
                }
                else if(count == directions.size() - 1) {
                    count++;
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                    stepBack.push(copyStart);
                    copyStart = start;
                }
                else{
                    count++;
                    //Log.d("direction", String.valueOf(directions.get(count)));
                    setDirections(directions);
                    stepBack.push(copyStart);
                    copyStart = id.get(0);
                    id.remove(0);
                    for(String s : stepBack){
                        Log.d("Stack", String.valueOf(s));
                    }
                }
                Log.d("Count", String.valueOf(count));
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
                            "Unable to skip. No exhibits left!");
                }
                else if(id.size() == 1){
                    count++;
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                    stepBack.push(copyStart);
                    copyStart = start;
                    id.remove(0);
                    directions.remove(directions.size() - 1);
//                    Utilities.showAlert(DirectionsActivity.this,
//                            "Only 1 exhibit left! Unable to skip.");
                }
                else {
                    for (int i = 0; i < id.size(); i++) {
                        Log.d("hi", id.get(i));
                    }
                    id.remove(0);
                    id = Route.sortExhibits(id, copyStart, g, vertexInfo, edgeInfo);
                    List<String> newDirections = Route.createRoute(id, copyStart, g, vertexInfo, edgeInfo);
                    directions.subList(count + 1, directions.size()).clear();
                    directions.addAll(newDirections);
                    //count = 0;
                    //id.remove(0);
                    count++;
                    setDirections(directions);
//                    for (int i = 0; i < id.size(); i++) {
//                        Log.d("hi", id.get(i));
//                    }
//                    for (int i = 0; i < id.size(); i++) {
//                        Log.d("hi", directions.get(i));
//                    }
                    stepBack.push(copyStart);
                    copyStart = id.get(0);
                    id.remove(0);
                    for(String s : stepBack){
                        Log.d("Stack", String.valueOf(s));
                    }
                }
            }
        });

        detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(count >= directions.size() - 1) {
//                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
//                }
//                else {
                    setDirections(directions);
//                }
            }
        });
    }

}