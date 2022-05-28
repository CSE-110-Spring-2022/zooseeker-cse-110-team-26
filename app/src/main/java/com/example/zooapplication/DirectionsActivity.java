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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DirectionsActivity extends AppCompatActivity {
    private List<String> directions;
    private ExhibitsItemDao dao;
    int count = 1;
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
//        for(String s :id){
//            Log.d("Test", String.valueOf(s));
//        }
        //stepBack.push(id.get(0));
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
                   count--;
                   String endPoint = stepBack.pop();
                   id.add(0, endPoint);
//                    for(String s: id){
//                        Log.d("Test", String.valueOf(s));
//                    }
                   String toPrevious = Directions.findPath(copyStart, endPoint, g, vertexInfo, edgeInfo);
                   displayDirection.setText(toPrevious);

                }
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
                count++;
                if(count >= directions.size()){
                    Utilities.showAlert(DirectionsActivity.this,
                            "The route is done!");
                }
                else{
                    //Log.d("direction", String.valueOf(directions.get(count)));
                    setDirections(directions);
                    stepBack.push(copyStart);
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
                }
            }
        });

        detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDirections(directions);
            }
        });
    }

}