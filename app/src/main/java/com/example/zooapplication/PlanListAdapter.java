package com.example.zooapplication;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {

    private List<PlanListItems> todoItems = Collections.emptyList();

    public void setPlanListItems(List<PlanListItems> newTodoItems){
        this.todoItems.clear();
        this.todoItems = newTodoItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.plan_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTodoItem(todoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    @Override
    public long getItemId(int position){
        return todoItems.get(position).id;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView;
        private PlanListItems todoItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            this.textView = itemView.findViewById(R.id.plan_list_item_text);
        }
        public PlanListItems getTodoItem(){ return todoItem;}

        public void setTodoItem(PlanListItems todoItem){
            this.todoItem = todoItem;
            this.textView.setText(todoItem.text);
        }

    }

}

