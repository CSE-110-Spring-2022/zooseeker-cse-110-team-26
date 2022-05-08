package com.example.zooapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ExhibitsActivity extends AppCompatActivity {
    AutoCompleteTextView autoComplete;
    ArrayList<String> resultName;
    ArrayList<String> resultId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibits);
        //contains what users have clicked
        resultName = new ArrayList<>();
        resultId = new ArrayList<>();
        //keep track of duplicate element in the dropdown list
        Set<String> duplicate = new HashSet<>();
        //Need to add all the 'tags' into list
        //because user will search for 'id'
        List<String> typeName = new LinkedList<>();
        // get singleton from database
        ExhibitsItemDao dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();

        //get all elements from database
        List<ExhibitsItem> list = dao.getAll();
        
        //add all strings in the tags.
        for(ExhibitsItem i : list){
            if(i.kind.equals("exhibit") && duplicate.add(i.name)){
                String temp = i.name.toLowerCase();
                typeName.add(temp);
            }
        }
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);

        //use custom adapter, maybe don't need custom adapter.
        ExhibitsItemAdapter adapter = new ExhibitsItemAdapter(this, typeName);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);
        //display the list of the clicked items.
        ListView view1 = findViewById(R.id.dis);
        //display the number of selected items
        TextView number = findViewById(R.id.number_of_exhibits);
        //listview custom view
        ArrayAdapter displayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultName);
        view1.setAdapter(displayAdapter);
        //one of the item in drop-down list is clicked.
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //the drop-down menu is base on "tags", so need to figure out the
                //correspond name of the item. And the suggestion should only
                //show the "exhibits"
                for(ExhibitsItem ex : list){
                    String temp = ex.name.toLowerCase();
                    //found the matched result
                    if(temp.contains(adapterView.getItemAtPosition(i).toString()) && ex.kind.equals("exhibit")){
                        //result does not in the display menu
                        if(!resultName.contains(ex.name)) {
                            resultId.add(ex.id);
                            resultName.add(ex.name);
                        }
                        //show error
                        else{
                            Utilities.showAlert(ExhibitsActivity.this, "Item already in the exhibits list");
                        }
                    }
                }
                //result has been dated, call adapter to update the UI
                displayAdapter.notifyDataSetChanged();
                //update the number of item
                number.setText(String.valueOf(resultName.size()));
                //clear the text box after click item
                autoComplete.getText().clear();
            }
        });
    }

    public void onPlanClicked(View view) {
        Intent intent = new Intent(this,DisplayPlanActivity.class);
        Gson gson = new Gson();
        //pass the list of the users have typed to the next activity
        String nameResult = gson.toJson(resultName);
        intent.putExtra("names", nameResult);
        String id = gson.toJson(resultId);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}