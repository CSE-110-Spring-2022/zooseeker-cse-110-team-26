package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExhibitsActivity extends AppCompatActivity {
    AutoCompleteTextView autoComplete;
    ArrayList<String> exhibitsId;
    Map<String, ZooData.VertexInfo> vertexInfo;
    Map<String, ZooData.EdgeInfo> edgeInfo;

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
            if(i.kind.equals("exhibit")){
                for(String s: i.tags){
                    if(duplicate.add(s)){
                        name.add(s);
                    }
                }
            }
        }
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);
        //contains what users have clicked
        exhibitsId = new ArrayList<>();
        //use custom adapter, maybe don't need custom adapter.
        ExhibitsItemAdapter adapter = new ExhibitsItemAdapter(this, name);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
        //display the list of the clicked items.
        ListView view1 = findViewById(R.id.dis);
        //display the number of selected items
        TextView number = findViewById(R.id.number_of_exhibits);
        //listview custom view
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, exhibitsId);
        view1.setAdapter(displayAdapter);

        //one of the item in drop-down list is clicked.
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //item already in the display list
                if(exhibitsId.contains(adapterView.getItemAtPosition(i).toString())){
                    Utilities.showAlert(ExhibitsActivity.this, "Item already in the exhibits list");
                }
               else{
                    //the drop-down menu is base on "tags", so need to figure out the
                    //correspond name of the item. And the suggestion should only
                    //show the "exhibits"
                    for(ExhibitsItem ex : list){
                        for(String s: ex.tags){
                            if(s.contains(adapterView.getItemAtPosition(i).toString()) && ex.kind.equals("exhibit")){
                                exhibitsId.add(ex.name);
                            }
                        }
                    }
                    //result has been dated, call adapter to update the UI
                    displayAdapter.notifyDataSetChanged();
                    //update the number of item
                    number.setText(String.valueOf(exhibitsId.size()));
                }

            }
        });
    }

    public void onPlanClicked(View view) {
        Intent intent = new Intent(this,DisplayPlanActivity.class);
        //pass the name that in the list to plan activity
        intent.putStringArrayListExtra("result", exhibitsId);
        startActivity(intent);
    }
}