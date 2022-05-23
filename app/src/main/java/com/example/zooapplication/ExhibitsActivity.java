/**
 * This file displays the search bar displaying and adding selected searched items
 */
package com.example.zooapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ExhibitsActivity extends AppCompatActivity {
    AutoCompleteTextView autoComplete;
    List<String> resultName;
    ArrayList<String> resultId;
    RecyclerView recyclerView;
    ArrayList<ExhibitsItem> result;
    Map<String, ExhibitsItem> map = new HashMap<>();
    ExhibitsItemDao dao;
    private ExhibitsItemViewModel viewModel;
    PlanListAdapter planListAdapter;
    List<ExhibitsItem> list;
    Button clearAll;
    TextView number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibits);
        //For live data
        viewModel = new ViewModelProvider(this).get(ExhibitsItemViewModel.class);
        result = new ArrayList<>();
        //contains what users have clicked, include the name and id
        resultName = new ArrayList<>();
        resultId = new ArrayList<>();
        //Need to add all the 'tags' into list
        List<String> typeName = new LinkedList<>();
        //clear all the list
        clearAll = findViewById(R.id.clear_all);
        //display the number of selected exhibits
        number = findViewById(R.id.number_of_exhibits);
        // get singleton from database
        dao = ExhibitsDatabase.getSingleton(this).exhibitsItemDao();
        //get all elements from database
        list = dao.getAll();
        //display the plan list by using recycleview with custom adapter
        planListAdapter = new PlanListAdapter();
        planListAdapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.dis);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //add all strings that in the tags to the dropdown menu.
        collectAllTags(typeName);
        recyclerView.setAdapter(planListAdapter);
        planListAdapter.setExhibitsItems(result);
        //Persist data when the app is closed
        viewModel.getExhibitsItems().observe(this, planListAdapter::setExhibitsItems);
        autoComplete = (AutoCompleteTextView)
                findViewById(R.id.search_bar);

        //use custom adapter, maybe don't need custom adapter.
        ExhibitsItemAdapter adapter = new ExhibitsItemAdapter(this, typeName);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter);

        //handle delete item
        deleteItem();

        //one of the item in drop-down list is clicked.
        getItemClickListener(planListAdapter);

        //update the number of selected exhibits
        List<ExhibitsItem> temp1 = dao.getLive();
        for(ExhibitsItem ex: temp1){
            resultName.add(ex.name.toLowerCase());
        }
        updateNumber(temp1.size());
        //clear the whole plan
        clearPlan();
        //save the last activity
        storeLastActivity();

    }

    /**
     * The list contains all the element's tags
     * The users will search tags
     * so we need to show all the words in tags.
     * @param typeName contains all words
     */
    private void collectAllTags(List<String> typeName) {
        for(ExhibitsItem i : list){
            for(String s : i.tags){
                //do not show duplicate element
                if(!typeName.contains(s)){
                    typeName.add(s);
                }
                map.put(s, i);
            }
        }
    }

    /**
     * Using SharePreferences
     * Preferences will record the last activity's name
     * If this activity is the last activity, then this class
     * name will be store in the preferences
     */
    private void storeLastActivity() {
        ShareData.setLastActivity(App.getContext(), "last activity", getClass().getName());
    }

    /**
     * When user click clear all.
     * All item will be delete
     */
    private void clearPlan() {
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear the list
                resultName.clear();
                resultId.clear();
                //update the displayed selected number
                updateNumber(resultName.size());
                //delete from our database
                List<ExhibitsItem> temp = planListAdapter.getExhibitsItems();
                for(ExhibitsItem ex : temp){
                    viewModel.deleteItem(ex);
                }
            }
        });
    }

    /**
     * When user clikc "X", a single item will be deleted
     * from the list
     */
    private void deleteItem() {
        planListAdapter.setOnDeleteClickedHandler(new Consumer<ExhibitsItem>() {
            @Override
            public void accept(ExhibitsItem exhibitsItem) {
                //delete elemene in the resultId AND resultName
                for(ExhibitsItem ex: list){
                    if(ex.name.equals(ex.name) && !ex.id.equals("add")){
                        resultId.remove(ex.id);
                    }
                }
                //remove the element from the list so that
                //users are able to add it back later.
                resultName.remove(exhibitsItem.name.toLowerCase());
                //update the displayed number of selected number;
                updateNumber(resultName.size());
                //delete item from the database;
                viewModel.deleteItem(exhibitsItem);
            }
        });
    }

    /**
     * update the number of selected exhibits
     * @param size
     */
    private void updateNumber(int size){
        ExhibitsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                number.setText(String.valueOf(size));
            }
        });
    }

    /**
     * user click a item that is showing in the drop down menu,
     * We need to let the adater to show the new item
     * @param planListAdapter
     */
    private void getItemClickListener(PlanListAdapter planListAdapter) {
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //the drop-down menu is base on "tags", so need to figure out the
                //correspond name of the item. And the suggestion should only
                //show the "exhibits"
                String tag = adapterView.getItemAtPosition(i).toString();
                //get the object from the map which we initialized in the collectAllTags method
                //Categorize each word,key is the word, the value is the animal's name.
                ExhibitsItem item = map.get(tag);
                String temp = item.name.toLowerCase();
                if(!resultName.contains(temp)){
                    //this is for custom adapter
                    result.add(item);
                    //contains the name and id that users have been clicked
                    //pass to the next activity
                    resultName.add(temp);
                    resultId.add(item.id);
                    //create a new object for live data, this is like a garbage, the most useful info
                    //is the name. When we want to retain the data, we can only get all the object that
                    //its id or kind is "add", I add a method in the database so that we can get those
                    //object
                    dao.insert(new ExhibitsItem("add", "add", new ArrayList<>(), item.name));
                    //result has been changed, call adapter to update the UI
                    planListAdapter.notifyDataSetChanged();
                    //update the number of item
                    updateNumber(resultName.size());


                }
                //the element that have been added to the plan list
                else{
                    Utilities.showAlert(ExhibitsActivity.this, "Item already in the exhibits list");
                }
                //clear the text box after click item
                autoComplete.getText().clear();

            }
        });
    }

    /**
     * save all needed information in Preferences, when we go to next activity.
     * We just can use ShareData.get....
     * In addition, every time we pass the context, we must use App.getContext().
     * We don't need to use it, but we must add it
     * @param view
     */
    public void onPlanClicked(View view) {
        Intent intent = new Intent(this,DisplayPlanActivity.class);
        Gson gson = new Gson();
        //We need to update the list every time we click button
        //so that we can get the newest list
        resultName.clear();
        resultId.clear();
        //get the currect item listed in the recycle view.
        //send list to the next activity
        resultName = planListAdapter.getExhibitsItem();
        //get the id list from the result name
        for(String s : resultName){
            for(ExhibitsItem ex : list){
                if(s.equals(ex.name) && !ex.id.equals("add")){
                    resultId.add(ex.id);
                }
            }
        }
        shareData();
        startActivity(intent);
    }

    /**
     * save data to the preferences
     */
    public void shareData(){
        Context context = App.getContext();
        Gson gson = new Gson();
        String name = gson.toJson(resultName);
        String id = gson.toJson(resultId);
        ShareData.setResultName(context, "result name", name);
        ShareData.setResultId(context, "result id", id);

    }

}