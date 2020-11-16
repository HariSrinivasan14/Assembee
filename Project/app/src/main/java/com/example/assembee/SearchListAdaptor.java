package com.example.assembee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchListAdaptor extends  RecyclerView.Adapter<SearchListAdaptor.ViewHolder> {
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> owners = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private Context context;

    public SearchListAdaptor(ArrayList<String> titles, ArrayList<String> owners, ArrayList<String> descriptions, Context context) {
        this.titles = titles;
        this.owners = owners;
        this.descriptions = descriptions;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_card_large, parent, false);
        return new SearchListAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.owner.setText(owners.get(position));
        holder.description.setText(descriptions.get(position));
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView owner;
        TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.project_card_title);
            owner = itemView.findViewById(R.id.project_card_owner);
            description = itemView.findViewById(R.id.project_card_description);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // send intent to notify the new project detail activity
                    Intent intent = new Intent(itemView.getContext(), ProjectDetail.class);
                    intent.putExtra("title", titles.get(getAdapterPosition()));
                    intent.putExtra("owner", owners.get(getAdapterPosition()));
                    intent.putExtra("description", descriptions.get(getAdapterPosition()));

                    context.startActivity(intent);
                }
            });

        }
    }
}
