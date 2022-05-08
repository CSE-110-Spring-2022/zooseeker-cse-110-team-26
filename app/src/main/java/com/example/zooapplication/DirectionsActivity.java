package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Map;

public class DirectionsActivity extends AppCompatActivity {
    private ArrayList<String> directions;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        Gson gson = new Gson();
        String str = getIntent().getStringExtra("names");
        directions = gson.fromJson(str, ArrayList.class);
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
    public void onPlanClicked(View view) {
        TextView textSetter = findViewById(R.id.currentDirection);
        if(count >= directions.size()){
            Utilities.showAlert(this, "Finished Plan!");
        }
        textSetter.setText(directions.get(count));
        count++;
    }
}