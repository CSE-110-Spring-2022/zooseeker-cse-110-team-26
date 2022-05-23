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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DirectionsActivity extends AppCompatActivity {
    private List<String> directions;
    private ExhibitsItemDao dao;
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
        Gson gson = new Gson();
        directions = new ArrayList<>();
        if(!ShareData.getNames(App.getContext(), "names").equals("")){
            String di = ShareData.getNames(App.getContext(), "names");
            directions = gson.fromJson(di, ArrayList.class);
        }
        else{
            String str = getIntent().getStringExtra("names");
            directions = gson.fromJson(str, List.class);
        }

        //Connects to UI
        displayDirection = findViewById(R.id.currentDirection);
        getNextDirection = findViewById(R.id.getNextDirection);
        goBack = findViewById(R.id.back);
        //Inputs the list of instructions passed in from DisplayPlayActivity


        //Connects the plan with the UI
        displayDirection.setText(directions.get(1));
        //Updates the Textview UI if the Next button is clicked
        getNextDirectionClicked();
        //If the goBack button is clicked, exit DirectionsAcitivty class
        goBackClicked();
        shareData();
    }

    public List<String> getDirections(){
        return this.directions;
    }
    private void shareData(){;
        Gson gson = new Gson();
        String s = gson.toJson(getDirections());
        ShareData.setLastActivity(App.getContext(), "last activity", getClass().getName());
        ShareData.setNames(App.getContext(), "names", s);
    }
    private void goBackClicked() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DisplayPlanActivity.class);
                finish();
                startActivity(intent);
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
                    displayDirection.setText(directions.get(count));
                }
            }
        });
    }

}