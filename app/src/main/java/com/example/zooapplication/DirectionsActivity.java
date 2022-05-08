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
    int count = 1;
    TextView displayDirection;
    Button getNextDirection;
    Button goBack;
    Iterator<String> it = null;
    private String start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        displayDirection = findViewById(R.id.currentDirection);
        getNextDirection = findViewById(R.id.getNextDirection);
        goBack = findViewById(R.id.back);
        Gson gson = new Gson();
        String str = getIntent().getStringExtra("names");
        directions = gson.fromJson(str, List.class);


        displayDirection.setText(directions.get(1));
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

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
}