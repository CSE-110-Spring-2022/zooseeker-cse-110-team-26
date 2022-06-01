/**
 * Class name: DirectionActivity
 * Description: In this activity, we need to hand to
 *              handle many things. Go next, step back
 *              , skip, go back, set mock location.
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
    String currentPoint;
    List<ExhibitsItem> planList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        //open database
        dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();
        //get all the exhibits with lat and lng
        exhibitsItems = dao.getAllWithLatLng();
        getStepBack = findViewById(R.id.step_back);
        stepBack = new Stack<>();
        Gson gson = new Gson();
        directions = new ArrayList<>();
        planList = new ArrayList<>();
        //get sorted plan list
        String di = ShareData.getNames(App.getContext(), "names");
        directions = gson.fromJson(di, ArrayList.class);
        String i = ShareData.getResultId(App.getContext(), "ids");
        String ex = ShareData.getExhibits(App.getContext(), "exhibits");
        //planList = gson.fromJson(ex, List.class);
        currentPoint = "";
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

        for(ExhibitsItem e: exhibitsItems){
            for(String s : id){
                if(e.id.equals(s) && !e.name.equals("add")){
                    planList.add(e);
                }
            }
        }

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

        //save this activity's data
        shareData();

        //set location button
        setLocationClicked();

        //step back button
        stepBackClicked();

        //skip button
        skipButtonClicked();

        //detail button
        detailClicked();
    }

    /**
     * step back button is clicked
     * show the current location the previous exhibits
     * Use stack to store the previous exhibits, the top
     * of the stack will be the first previous exhibit
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
     * set mock location,
     * if the closest point of the input location
     * is the next exhibit of the route plan, then
     * we don't need to re-plan, because the user is on the
     * right direction, we just need to update the UI
     * Otherwise, it should pop up a window to ask user if
     * they want to re-plan or not.
     */
    private void setLocationClicked() {
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make sure set a legal lat and lng;
                if(userLat.getText().toString().equals("") || userLng.getText().toString().equals("")){
                    Utilities.showAlert(DirectionsActivity.this, "Enter numeric for lat and lng! ");
                    return;
                }
                if(count < 0 || count > id.size()){
                    return;
                }
                //get lat and lng from the input
                double lat = Double.parseDouble(userLat.getText().toString());
                double lng = Double.parseDouble(userLng.getText().toString());
                Coord inputLocation = new Coord(lat, lng);
                ExhibitsItem newItem = new ExhibitsItem("","",new ArrayList<>(), "", "", 0.0, 0.0);
                boolean needToReplan = false;
                String newStart = "";
                double tempDis = Double.MAX_VALUE;
                Coord cloestPoint = inputLocation;
                //find input point
                for(ExhibitsItem ex : exhibitsItems){
                    Coord current = new Coord(ex.lat, ex.lng);
                    double temp = Coord.getDist(current,  inputLocation);
                    if(temp < tempDis){
                        tempDis = temp;
                        newItem = ex;
                        newStart = ex.id;
                        cloestPoint = current;
                    }
                }

                copyStart = newStart;
                //currentPoint = copyStart;

                // if the set location is not in the graph, (dis != 0)
                // 1. first find the nearest node N
                // 1.1 find the closest point A(in the list) of N
                //     1.1.1. if A is id.get(0); no need to replan, update ui
                //     else use N replan needToReplan = true;

                tempDis = Double.MAX_VALUE;
//              find the closest point of above loop
                for(ExhibitsItem ex: planList){
                    Coord cur = new Coord(ex.lat, ex.lng);
                    double temp = Coord.getDist(cur, cloestPoint);
                    if(temp < tempDis){
                        tempDis = temp;
                        newItem = ex;
                    }
                }

                if(tempDis != 0.0d && newItem.id.equals(id.get(count))){
                    needToReplan = false;
                    String s = Directions.findPath(copyStart, id.get(count), g, vertexInfo, edgeInfo);
                    setDirections(s);
                    //String s = Directions.findPath(copyStart, id.get(count), g, vertexInfo, edgeInfo);
                }

                else if(tempDis == 0.0d && newItem.id.equals(id.get(count))){
                    needToReplan = false;
//                    String s = Directions.findPath(copyStart, id.get(count), g, vertexInfo, edgeInfo);
//                    setDirections(s);
                    return;
                }


                else{
                    needToReplan = true;
                }

                //construct a alert box;
                AlertDialog.Builder builder = new AlertDialog.Builder(DirectionsActivity.this);
                builder.setMessage("Off-route! Want to re-plan?");
                //build a re-plan button
                builder.setPositiveButton("re-plan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        replan();
                    }
                });
                //build a cancel button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //The location is not on the right direction, show alert box
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
                vertexInfo = ZooData.loadVertexInfoJSON("exhibit_info.json",
                        DirectionsActivity.this);
                edgeInfo = ZooData.loadEdgeInfoJSON("trail_info.json",
                        DirectionsActivity.this);
                g = ZooData.loadZooGraphJSON("zoo_graph.json",
                        DirectionsActivity.this);

            }
        });
    }

    /**
     * re-plan if the user's location is set
     * and the current location is off-route
     *
     */
    private void replan() {
        //The input location is one the exhibits that in the unvisited list
        if(id.contains(copyStart)){
            Log.d("check exist", String.valueOf(id.contains(copyStart)));
            id.remove(copyStart);
        }
        List<String> toAppend = new LinkedList<>();
        //rearrange the sublist
        toAppend = Route.sortExhibits(id.subList(count, id.size()), copyStart, g, vertexInfo, edgeInfo);
        String s = Directions.findPath(copyStart, toAppend.get(0),g,vertexInfo, edgeInfo);
        if(!s.equals("")){
            setDirections(s);
        }
        else{
            String str = "We are already at " + toAppend.get(0);
            setDirections(str);
        }
        id.subList(count, id.size()).clear();
        id.addAll((toAppend));
    }

    /**
     * save last activity and necessary information
     * This is for return to this UI if the app is killed
     * and the last activity is this activity
     */
    private void shareData(){;
        ShareData.setLastActivity(App.getContext(), "last activity", getClass().getName());
    }

    /**
     * go back button is clicked
     * go back to the previous UI
     */
    private void goBackClicked() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayPlanActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    /**
     * go next button is clicked
     * case 1: the route is done, do nothing;
     * case 2: at the last exhibit, show the direction
     *         from curren location to the exit gate
     * case 3: show direction from current loction to
     *         the next exhibit
     * Push the current location to the stack
     * so that we can handle the step back
     * direction.
     */
    private void getNextDirectionClicked() {
        getNextDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //no next exhibit and we are at the exit gate
                if(count >= id.size()){
                    Utilities.showAlert(DirectionsActivity.this,
                            "The route is done!");
                }
                //We are at the last exhibit, then show the direction to the
                //exit gate if next button is clicked.
                else if(count == id.size() - 1) {
                    count++;
                    String s = Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo);
                    setDirections(s);
                    stepBack.push(copyStart);
                }
                //show the next exhibit
                else{
                    count++;
                    //Log.d("direction", String.valueOf(directions.get(count)));
                    setDirections(Directions.findPath(copyStart, id.get(count), g, vertexInfo,edgeInfo));
                    currentPoint = id.get(count);
                    stepBack.push(copyStart);
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
     * if the switch if off, just show the brief  description
     * otherwise, display the detailed description
     */
    private void detailClicked() {
        detailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //users are at the entrance.
                if(count < 0){
                    return;
                }
                //users are at the exit gate
                if(count >= id.size()) {
                    setDirections(Directions.findPath(copyStart, start, g, vertexInfo, edgeInfo));
                }
                //display the detail message
                else {
                    setDirections(Directions.findPath(copyStart, id.get(count), g, vertexInfo, edgeInfo));
                }

            }
        });
    }

    /**
     * handle skip operation
     * case 1: If the users already at the last exhibit
     *         then they can't skip
     * case 2: There is only 1 exhibit left, then we can skip that
     *         one and show the direction to the exit gate
     * case 3: skip the next exhibit and re-plan the route.
     */
    private void skipButtonClicked() {
        skipDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //if we are at the last exhibit so we can't skip the next exhibit
                if(id.size() == 0){
                    Utilities.showAlert(DirectionsActivity.this,
                            "Unable to skip. No exhibits left!");
                }
                //skip the last exhibit then just lead the users to the exit gate
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
                //skip the next exhibit and re-calculate the route
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

    private void setDirections(String directions){
        if(detailed.isChecked()) {
            displayDirection.setText(directions);
        }
        else {
            displayDirection.setText(DetailedtoBrief.toBrief(directions));
        }
    }

}

