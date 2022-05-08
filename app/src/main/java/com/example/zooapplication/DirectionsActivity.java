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
import java.util.List;
import java.util.Map;

public class DirectionsActivity extends AppCompatActivity {
    private List<String> directions;
    int count = 0;
    TextView displayDirection;
    Button getNextDirection;
    Iterator<String> it = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        displayDirection = findViewById(R.id.currentDirection);
        getNextDirection = findViewById(R.id.getNextDirection);
        Gson gson = new Gson();
        String str = getIntent().getStringExtra("names");
        directions = gson.fromJson(str, List.class);

        //Log.d("direction", String.valueOf(directions.size()));
        displayDirection.setText(directions.get(0));
        getNextDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count >= directions.size()){
                    Utilities.showAlert(DirectionsActivity.this, "The exhibits list is empty!");
                }
                else{
                    displayDirection.setText(directions.get(count));
                }
            }
        });

        /*
        Calls the algorithm and the algorithm should return a List of Strings of Directions from
        starting parting point to first node to be visited, also need to return the destination somehow

        When we click NEXT:
            Things we need to pass into next intent:
            Graph g
            String start (the new current node)
            List<String> unvisited, removing the new current node from that list
            Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo
 */
    }
//    public void onPlanClicked(View view) {
//        TextView textSetter = findViewById(R.id.currentDirection);
//        if(count >= directions.size()){
//            Utilities.showAlert(this, "Finished Plan!");
//        }
//        textSetter.setText(directions.get(count));
//        count++;
//    }
}