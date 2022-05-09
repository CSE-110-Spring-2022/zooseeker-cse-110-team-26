/**
 * This file contains the the methods and UI for the screens that display the directions
 */
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
        //Connects to UI
        displayDirection = findViewById(R.id.currentDirection);
        getNextDirection = findViewById(R.id.getNextDirection);
        goBack = findViewById(R.id.back);
        //Inputs the list of instructions passed in from DisplayPlayActivity
        Gson gson = new Gson();
        String str = getIntent().getStringExtra("names");
        directions = gson.fromJson(str, List.class);

        //Connects the plan with the UI
        displayDirection.setText(directions.get(1));
        //Updates the Textview UI if the Next button is clicked
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
                    displayDirection.setText(directions.get(count));
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
    }
}