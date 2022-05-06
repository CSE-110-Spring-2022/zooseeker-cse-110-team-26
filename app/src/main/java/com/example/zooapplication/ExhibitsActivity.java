package com.example.zooapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ExhibitsActivity extends AppCompatActivity {
    AutoCompleteTextView autoComplete;
    public RecyclerView recyclerView;
    ArrayList<String> result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibits);

        // get singleton from database
        ExhibitsItemDao dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();
        //get all elements from database
        List<ExhibitsItem> list = dao.getAll();

        //Need to add all the 'tags' into list
        //because user will search for 'id'
        List<String> name = new LinkedList<>();
        //keep track of duplicate element in the dropdown list
        Set<String> duplicate = new HashSet<>();

        //add all strings in the tags.
        for(ExhibitsItem i : list){
            if(i.itemType.equals("exhibit")){
                for(String s: i.tags){
                    if(duplicate.add(s)){
                        name.add(s);
                    }
                }
            }
        }
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);
        result = new ArrayList<>();
        ExhibitsItemAdapter adapter = new ExhibitsItemAdapter(this, name);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
        TextView view1 = findViewById(R.id.display);
        DisplayAdapter displayAdapter = new DisplayAdapter();
        displayAdapter.setHasStableIds(true);
        displayAdapter.setStringList(name);
//        recyclerView = findViewById(R.id.recycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(displayAdapter);
        //displayAdapter.setStringList(adapter.getSuggestions());

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                result.add(adapterView.getItemAtPosition(i).toString());
                Log.d("test", String.valueOf(result.size()));
                view1.setText(adapterView.getItemAtPosition(i).toString());
            }
        });

    }

    public void onPlanClicked(View view) {
        Intent intent = new Intent(this,DirectionsActivity.class);
        intent.putStringArrayListExtra("result", result);
        startActivity(intent);
        /*
            Need to use sample_node_info to populate the database.
            We need to edit the search bar so that you can click a tag, but inside the travel
            list it will show the name
            When we click the Plan button, go to the next activity WHICH
            Calls the algorithm and the algorithm should return a List of Strings of Directions from
            starting parting point to first node to be visited.
            Things we need to pass into next intent:
            Graph g
            String start (the new current node)
            List<String> unvisited, removing the new current node from that list
            Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo
        */
    }
}