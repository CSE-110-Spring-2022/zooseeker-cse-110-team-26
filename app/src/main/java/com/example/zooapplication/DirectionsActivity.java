/**
 * This file contains the the methods and UI for the screens that display the directions
 */
package com.example.zooapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button set;
    private final String start = "entrance_exit_gate";
    List<String> id;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;
    Graph g;
    String copyStart = start;
    Stack<String> stepBack;
    EditText userLat;
    EditText userLng;
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
        dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();
        List<ExhibitsItem> exhibitsItems = dao.getAllWithLatLng();
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
        set = findViewById(R.id.set);
        userLat = findViewById(R.id.lat);
        userLng = findViewById(R.id.lng);

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

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //get users lat and lng and store in coord
//                double lat = Double.parseDouble(userLat.getText().toString());
//                double lng = Double.parseDouble(userLng.getText().toString());
//                Coord userCoord = new Coord(lat, lng);
//                double firstLat = vertexInfo.get(id.get(0)).lat;
//                double firstLng = vertexInfo.get(id.get(0)).lng;
//                double firstToUserDist = userCoord.getDist(new Coord(firstLat, firstLng), userCoord);
//                boolean needToReplan = false;
//                Coord current = userCoord;
//                for (String s: id){
//                    lat = vertexInfo.get(s).lat;
//                    lng = vertexInfo.get(s).lng;
//                    Coord newCoord = new Coord(lat, lng);
//                    if(userCoord.getDist(newCoord, current) < firstToUserDist){
//                        needToReplan = true;
//                        break;
//                    }
//                    current = newCoord;
//                }
                boolean needToReplan = true;
                if(needToReplan){
                    //Need to show alert w/ yes or no option
                    //if yes, replan
                    //if no, exit alert
                    AlertDialog.Builder builder = new AlertDialog.Builder(DirectionsActivity.this);
                    builder.setMessage("Off-route! Want to re-plan?");
                    builder.setPositiveButton("re-plan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            replan();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog di = builder.create();
                    di.show();
                }
            }
        });

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

    private void replan() {
        double lat = Double.parseDouble(userLat.getText().toString());
        double lng = Double.parseDouble(userLng.getText().toString());


        Coord inputStart = new Coord(lat, lng);
        String newStart = "";
        double distance = Integer.MAX_VALUE;
        for(String s: id){
//            double lat1 = vertexInfo.get(s).lat;
//            double lng2 = vertexInfo.get(s).lng;
            Coord coord = new Coord(vertexInfo.get(s).lat, vertexInfo.get(s).lng);
            double temp  = Coord.getDist(coord, inputStart);
            if(distance > temp){
                distance = temp;
                newStart = s;
            }
        }
        id.remove(newStart);
        id = Route.sortExhibits(id, newStart, g, vertexInfo, edgeInfo);
        List<String> newRoute = Route.createRoute(id, newStart, g, vertexInfo, edgeInfo);
        directions.subList(count + 1, directions.size()).clear();
        directions.addAll(newRoute);
        count++;
        setDirections(directions);

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
                if(count >= directions.size() - 1) {
                    copyStart = stepBack.peek();
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                    copyStart = start;
                }
                else {
                    setDirections(directions);
                }
            }
        });
    }

}