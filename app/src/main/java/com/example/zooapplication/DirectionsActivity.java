package com.example.zooapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DirectionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
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