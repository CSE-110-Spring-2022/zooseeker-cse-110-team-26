package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

public class SetLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        //Button setButton = findViewById(R.id.set);
        EditText userLat = findViewById(R.id.lat);
        EditText userLng = findViewById(R.id.lng);
        Gson gson = new Gson();
        String di = ShareData.getNames(App.getContext(), "names");
//        setButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
}