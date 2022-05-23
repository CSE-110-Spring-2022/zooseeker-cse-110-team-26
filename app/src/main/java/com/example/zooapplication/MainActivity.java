package com.example.zooapplication;
/**
 * Main activity for starting our app.
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        Class<?> activityClass;
        try{
//            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
//            String temp = prefs.getString("direction", "null");
//            activityClass = Class.forName(prefs.getString("last activity", ExhibitsActivity.class.getName()));
//            intent = new Intent(MainActivity.this, activityClass);
//            intent.putExtra("names", temp);
            String name = ShareData.getLastActivity(App.getContext(),"last activity");
            activityClass = Class.forName(name);
            intent = new Intent(MainActivity.this, activityClass);

        }catch (ClassNotFoundException ex){
            intent = new Intent(MainActivity.this, ExhibitsActivity.class);

        }
        startActivity(intent);
        finish();
    }



}