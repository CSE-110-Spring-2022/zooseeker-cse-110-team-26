/**
 * This file contains the the methods and UI for the screens that display the directions
 */
package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
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

        for(String s : id){
            Log.d("Test", String.valueOf(s));
        }
        for(String s : directions){
            Log.d("Test", String.valueOf(s));
        }

        stepBack.push(start);
        /*for(int i = 0; i < id.size(); i++){
            Log.d("test", id.get(i));
        }
        */
        //Connects the plan with the UI
        setDirections(Directions.findPath(copyStart, id.get(count),g,vertexInfo,edgeInfo));
        //Updates the Textview UI if the Next button is clicked
        getNextDirectionClicked();
        //If the goBack button is clicked, exit DirectionsAcitivty class
        goBackClicked();

        shareData();

        stepBackClicked();

        setLocationClicked();

        stepBackClicked();

        skipButtonClicked();

        detailClicked();
    }

    /**
     * step back button is clicked
     */
    private void stepBackClicked() {
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
    }

    /**
     * set mock location
     */
    private void setLocationClicked() {
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Need to show alert w/ yes or no option
                //if yes, replan
                //if no, exit alert
                if(userLat.getText().toString().equals("") || userLng.getText().toString().equals("")){
                    Utilities.showAlert(DirectionsActivity.this, "Enter numeric for lat and lng! ");
                    return;
                }
                double lat = Double.parseDouble(userLat.getText().toString());
                double lng = Double.parseDouble(userLng.getText().toString());
                //Coord inputStart = new Coord(lat, lng);
                String inputPoint = "";
                double shortestDis = Double.MAX_VALUE;
                double temp = 0.0d;
                boolean needToReplan = false;
                String newStart = "";
                //find the point input point
                for(ExhibitsItem ex : exhibitsItems){
                    if(ex.lat == lat && ex.lng == lng){
                        newStart = ex.id;
                        break;
                    }
                }
                List<String> tempSortList = new ArrayList<>(id.subList(count, id.size()));
                tempSortList = Route.sortExhibits(tempSortList, newStart, g, vertexInfo, edgeInfo);
                copyStart = newStart;
                if(tempSortList.get(0) == id.get(count)){
                    if(id.contains(newStart)){
                        id.remove(newStart);
                    }
                    List<String> toAppend = new ArrayList<>();
                    toAppend = Route.sortExhibits(id.subList(count, id.size()), copyStart, g, vertexInfo, edgeInfo);
                    id.subList(count, id.size()).clear();
                    id.addAll(toAppend);
                    needToReplan = false;
                    String s = Directions.findPath(newStart, tempSortList.get(0), g, vertexInfo, edgeInfo);
                    setDirections(s);

                }
                else{
                    needToReplan = true;
                }



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
                //Log.d("check exist", String.valueOf(id.contains(copyStart)));
                if(needToReplan){
                    Dialog builder1 = builder.create();
                    builder1.show();
                }
            }
        });

    }

    /**
     * load zoo data from json
     */
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

            }
        });
    }


    /**
     * replan if the user's location is set
     */
    private void replan() {
        if(id.contains(copyStart)){
            Log.d("check exist", String.valueOf(id.contains(copyStart)));
            id.remove(copyStart);
        }
        List<String> toAppend = new LinkedList<>();
        toAppend = Route.sortExhibits(id.subList(count, id.size()), copyStart, g, vertexInfo, edgeInfo);
        String s = Directions.findPath(copyStart, toAppend.get(0),g,vertexInfo, edgeInfo);
        setDirections(s);
        id.subList(count, id.size()).clear();
        id.addAll((toAppend));

    }

    /**
     * save last activity and necessary infomation
     */

    private void shareData(){;
        ShareData.setLastActivity(App.getContext(), "last activity", getClass().getName());
    }

    /**
     * go back button is clicked
     */
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

    /**
     * go next button is clicked
     */
    private void getNextDirectionClicked() {
        getNextDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count >= id.size()){
                    Utilities.showAlert(DirectionsActivity.this,
                            "The route is done!");
                }
                else if(count == id.size() - 1) {
                    count++;
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                    stepBack.push(copyStart);
                }

                else{
                    count++;
                    //Log.d("direction", String.valueOf(directions.get(count)));

                    setDirections(Directions.findPath(copyStart, id.get(count), g, vertexInfo,edgeInfo));
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
    }

    /**
     * detail switch is switched
     */
    private void detailClicked() {
        detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count >= id.size()) {
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                }
                else {
                    setDirections(Directions.findPath(copyStart, id.get(count), g, vertexInfo, edgeInfo));
                }

            }
        });
    }

    private void skipButtonClicked() {
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
                    List<String> toAppend;
                    Log.d("toRemove" , "toRemove" + id.get(count));
                    id.remove(count);
                    toAppend = Route.sortExhibits(id.subList(count, id.size()), copyStart, g, vertexInfo, edgeInfo);
                    id.subList(count, id.size()).clear();
                    id.addAll(toAppend);
/*                     List<String> newDirections = Route.createRoute(id, copyStart, g, vertexInfo, edgeInfo);
                    directions.subList(count + 1, directions.size()).clear();
                    directions.addAll(newDirections);*/
                    //count = 0;
                    //id.remove(0);
                    //count++;
                    //setDirections(directions);
                    setDirections(Directions.findPath(copyStart, id.get(count), g, vertexInfo, edgeInfo));
//                    for (int i = 0; i < id.size(); i++) {
//                        Log.d("hi", id.get(i));
//                    }
//                    for (int i = 0; i < id.size(); i++) {
//                        Log.d("hi", directions.get(i));
//                    }
                    //stepBack.push(copyStart);
                    //copyStart = id.get(0);
                    //id.remove(0);
                    //for(String s : stepBack){
                    //    Log.d("Stack", String.valueOf(s));
                   // }
                }
            }
        });
    }

}

