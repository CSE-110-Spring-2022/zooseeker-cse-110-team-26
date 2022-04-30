package com.example.zooapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * custom adapter, to show the dropdown list
 */
public class ExhibitsItemAdapter extends ArrayAdapter<ExhibitsItem> {
    private final List<ExhibitsItem> suggestions;
    public ExhibitsItemAdapter(@NonNull Context context,@NonNull List<ExhibitsItem> originalList) {
        super(context, 0, originalList);
        suggestions = new ArrayList<>(originalList);
    }

    @Override
    public Filter getFilter(){
        return filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.exhibits_items, parent, false);

        }
        TextView textView = convertView.findViewById(R.id.exhibits_items);
        ExhibitsItem exhibitsItem = getItem(position);
        if(exhibitsItem != null){
            textView.setText(exhibitsItem.getId());
        }
        return convertView;
    }





    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            //store the suggestion;
            FilterResults filterResults = new FilterResults();
            List<ExhibitsItem> exhibitsItemList = new ArrayList<>();
            //users don't type anything
            if(charSequence == null || charSequence.length() == 0){
                exhibitsItemList.addAll(suggestions);
            }
            else{
                //get whatever uses type
                String word = charSequence.toString().toLowerCase().trim();
                for(ExhibitsItem ex : suggestions){
                    if(ex.getId().contains(word)){
                        exhibitsItemList.add(ex);
                    }
                }
            }
            filterResults.values = exhibitsItemList;
            filterResults.count = exhibitsItemList.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}