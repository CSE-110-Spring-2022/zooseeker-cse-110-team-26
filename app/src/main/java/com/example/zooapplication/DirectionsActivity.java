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

    //String source = "entrance_exit_gate";

    TextView displayDirection;
    List<ExhibitsItem> exhibitsItems;
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
        exhibitsItems = dao.getAllWithLatLng();
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

        loadZooData();

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

        //copyStart = id.get(0);
        //id.remove(0);

        stepBack.push(start);
        /*for(int i = 0; i < id.size(); i++){
            Log.d("test", id.get(i));
        }
        */
        //Connects the plan with the UI
        setDirections(Directions.findPath(copyStart,id.get(count),g,vertexInfo,edgeInfo));
        //Updates the Textview UI if the Next button is clicked
        getNextDirectionClicked();
        //If the goBack button is clicked, exit DirectionsAcitivty class
        goBackClicked();
        shareData();

// <<<<<<< HEAD

        getStepBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("STEP Back: " + copyStart);

                if (count == 0) {
                    count--;
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                } else if (count < 0) {
                    Utilities.showAlert(DirectionsActivity.this, "No previous item!");
                } else {
                    count--;
                    String endPoint = id.get(count);
//                    if(count != directions.size()){
//                        id.add(0, copyStart);
//                    }
//                    count--;
//                    for(String s: id){
//                        Log.d("Test", String.valueOf(s));
//                  }
                    Log.d("stepBack", "Copy Start: " + copyStart);
                    Log.d("stepBack", "EndPoint: " + endPoint);

                    System.out.println(endPoint);

                    String toPrevious = Directions.findPath(copyStart, endPoint, g, vertexInfo, edgeInfo);
                    setDirections(toPrevious);


                }
                Log.d("Count", String.valueOf(count));
            }
        });
// =======
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to show alert w/ yes or no option
                //if yes, replan
                //if no, exit alert
                AlertDialog.Builder builder = new AlertDialog.Builder(DirectionsActivity.this);
                builder.setMessage("Off-route! Want to re-plan?");
                int counter = 0;
                builder.setPositiveButton("re-plan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        double lat = Double.parseDouble(userLat.getText().toString());
                        double lng = Double.parseDouble(userLng.getText().toString());
//                        double lat;
//                        double lng;
//                        lat = 32.74476120197887;
//                        lng = -117.18369973246877;
                        Coord inputStart = new Coord(lat, lng);
                        double shortestDis = Double.MAX_VALUE;
                        for(ExhibitsItem ex : exhibitsItems){
                            Coord coord = new Coord(ex.lat, ex.lng);
                            double temp = Coord.getDist(inputStart, coord);
                            Log.d("new start", String.valueOf(ex.id));
                            Log.d("new dis", String.valueOf(temp));
                            if(temp < shortestDis){
                                shortestDis = temp;
                                copyStart = ex.id;

                            }
                        }
                        Log.d("new copy", String.valueOf(copyStart));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //Log.d("new start", String.valueOf(copyStart));
                if(id.contains(copyStart)){
                    Log.d("check exist", String.valueOf(id.contains(copyStart)));
                    id.remove(copyStart);

                }
                Log.d("check exist", String.valueOf(id.contains(copyStart)));
                AlertDialog di = builder.create();
                di.show();
            }
        });

        //setLocationClicked();

        stepBackClicked();
    }


    private void loadZooData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vertexInfo = ZooData.loadVertexInfoJSON("sample_node_info.json",
                        DirectionsActivity.this);
                edgeInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json",
                        DirectionsActivity.this);
                g = ZooData.loadZooGraphJSON("sample_zoo_graph.json",
                        DirectionsActivity.this);

// >>>>>>> origin/BugFix
            }
        });
    }

    private void setLocationClicked() {
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to show alert w/ yes or no option
                //if yes, replan
                //if no, exit alert
                AlertDialog.Builder builder = new AlertDialog.Builder(DirectionsActivity.this);
                builder.setMessage("Off-route! Want to re-plan?");
                builder.setPositiveButton("re-plan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        double lat = Double.parseDouble(userLat.getText().toString());
                        double lng = Double.parseDouble(userLng.getText().toString());
                        Coord inputStart = new Coord(lat, lng);
                        double shortestDis = Double.MAX_VALUE;
                        for(ExhibitsItem ex : exhibitsItems){
                            Coord coord = new Coord(ex.lat, ex.lng);
                            double temp = Coord.getDist(inputStart, coord);
                            if(temp < shortestDis){
                                temp = shortestDis;
                                copyStart = ex.id;

                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Log.d("new start", String.valueOf(copyStart));
                AlertDialog di = builder.create();
                di.show();
            }
        });
    }

    private void stepBackClicked() {
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
                    setDirections(toPrevious);
                    copyStart = endPoint;
                }
                Log.d("Count", String.valueOf(count));
            }
        });
    }

//    private void replan() {
//        double lat = Double.parseDouble(userLat.getText().toString());
//        double lng = Double.parseDouble(userLng.getText().toString());
//        Coord inputStart = new Coord(lat, lng);
//        double shorestDis = Double.MAX_VALUE;
//        String newStart = "";
//        //find the closest exhibit from the input
//        for(ExhibitsItem ex : exhibitsItems){
//            Coord coord = new Coord(ex.lat, ex.lng);
//            double temp = Coord.getDist(inputStart, coord);
//            if(temp < shorestDis){
//                temp = shorestDis;
//                newStart = ex.id;
//
//            }
//        }
//        id = Route.sortExhibits(id, newStart, g, vertexInfo, edgeInfo);
//        List<String> newRoute = Route.createRoute(id, newStart, g, vertexInfo, edgeInfo);
//        directions.subList(count + 1, directions.size()).clear();
//        directions.addAll(newRoute);
//        count++;
//        setDirections(directions);
//
//    }


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
                }

                else{
                    count++;
                    //Log.d("direction", String.valueOf(directions.get(count)));

                    setDirections(Directions.findPath(copyStart,id.get(count), g, vertexInfo,edgeInfo));
                    stepBack.push(copyStart);
//                    copyStart = id.get(0);
                    //id.remove(0);

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

