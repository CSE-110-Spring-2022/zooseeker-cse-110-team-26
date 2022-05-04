package com.example.zooapplication;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ViewHolder>{

    private List<String> displayResult = Collections.emptyList();

    void setStringList(List<String> newList){
        this.displayResult.clear();
        this.displayResult = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_exhibits,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setResult(displayResult.get(position));

    }

    @Override
    public int getItemCount() {
        return displayResult.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private String result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.display);
        }
        public String getResult(){
            return result;
        }

        public void setResult(String result){
            this.result = result;
            this.textView.setText(result);
        }
    }
}
