package com.example.zooapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Class name: PlanLisAdapter
 * Description: This adapter is for showing the plan list
 *              And update the UI whenever the users delete item
 *              from the list.
 *              This one used to be a listview, right now it a
 *              recycleview.
 */
public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {
    private List<ExhibitsItem> exhibitsItems = Collections.emptyList();
    private Consumer<ExhibitsItem> onDeleteClicked;

    /**
     * Delete button clicked.
     * @param onDeleteClicked
     */
    public void setOnDeleteClickedHandler(Consumer<ExhibitsItem> onDeleteClicked){
        this.onDeleteClicked = onDeleteClicked;
    }
    public void setExhibitsItems(List<ExhibitsItem> newExhibitsItem){
        this.exhibitsItems.clear();
        this.exhibitsItems = newExhibitsItem;
        notifyDataSetChanged();
    }

    /**
     * Contains whatever are showing in the plan list.
     * @return
     */
    public List<String> getExhibitsItem(){
        List<String> temp = new ArrayList<>();
        for(ExhibitsItem ex : this.exhibitsItems){
            temp.add(ex.name);
        }
        return temp;
    }
    public List<ExhibitsItem> getExhibitsItems(){
        return this.exhibitsItems;
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
        holder.setExhibitsItem(exhibitsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return exhibitsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView textView;
        private ExhibitsItem exhibitsItem;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.plan_list_item_text);

            this.deleteButton = itemView.findViewById(R.id.delete_btn);

            this.deleteButton.setOnClickListener(view -> {
                if(onDeleteClicked == null) return;
                onDeleteClicked.accept(exhibitsItem);

            });
        }

        public ExhibitsItem getExhibitsItem(){return this.exhibitsItem;}

        public void setExhibitsItem(ExhibitsItem exhibitsItem){
            this.exhibitsItem = exhibitsItem;
            this.textView.setText(exhibitsItem.name);
        }
    }
}
