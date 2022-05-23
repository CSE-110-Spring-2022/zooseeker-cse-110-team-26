package com.example.zooapplication;
/**
 * File Name: ExhibitsItemAdapter.java
 * Description: This file is for customizing adapter.
 *              Decide how to display the items.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Class: ExhibitsItemAdapter
 * Descrition: The main activity pass the
 *             the original list that contains
 *             all the animal's name
 *  Field:               suggestions     - contains all the animal's name
 *                       result          - contains the matched result
 * public function:      setSuggestion   - update the list if it's changed
 *                       getFilter       - get the filter result
 */
public class ExhibitsItemAdapter extends ArrayAdapter<String> {
    private List<String> suggestions;
    private List<String> result;

    //constructor
    public ExhibitsItemAdapter(@NonNull Context context,@NonNull List<String> originalList) {
        super(context, 0, originalList);
        //copy the whole list that contains all strings in "tags"
        suggestions = new ArrayList<>(originalList);
        result = new LinkedList<>();
    }


    @Override
    public Filter getFilter(){
        return filter;
    }

    /**
     * update our UI
     * @param position, position in the list that match the prefix from
     *                  search bar
     * @param convertView, current view(UI)
     * @param parent
     * @return, updated view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.exhibits_items, parent, false);

        }
        TextView textView = convertView.findViewById(R.id.exhibits_items);
        String exhibitsItem = getItem(position);
        //decide what we need to display in the UI
        if(exhibitsItem != null){
            //result.add(exhibitsItem);
            textView.setText(exhibitsItem);
        }
        return convertView;
    }


    /**
     * search our database from whatever uses type in
     * the search bar
     */
    private Filter filter = new Filter() {
        @Override
        //searching from data from our database
        protected FilterResults performFiltering(CharSequence charSequence) {
            //store the match results;
            FilterResults filterResults = new FilterResults();
            List<String> exhibitsItemList = new ArrayList<>();
            //users don't type anything
            if(charSequence == null || charSequence.length() == 0){
                exhibitsItemList.addAll(suggestions);
            }
            else{
                //get whatever uses type
                String word = charSequence.toString().toLowerCase().trim();
                //get all matched string and add them to
                //exhibitsItemList in order to assign to filterResulte.
                for(String ex : suggestions){
                    if(ex.contains(word)){
                        exhibitsItemList.add(ex);
                    }
                }
            }
            filterResults.values = exhibitsItemList;
            filterResults.count = exhibitsItemList.size();
            //this results contains all the matched items.
            return filterResults;
        }

        //use this new results to update our UI.
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //clear the the current drop-down menu
            clear();
            //need to show the matched list
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}