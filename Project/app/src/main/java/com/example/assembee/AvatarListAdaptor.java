package com.example.assembee;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.util.ArrayList;

public class AvatarListAdaptor extends RecyclerView.Adapter<AvatarListAdaptor.ViewHolder> {
    private ArrayList<String> names = new ArrayList<>();
    private Context context;

    public AvatarListAdaptor(ArrayList<String> names, Context context) {
        this.names = names;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contributor_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // count me in button
                    if (getAdapterPosition() == names.size() - 1) {
                        new MaterialAlertDialogBuilder(context)
                                .setTitle("Request to join this project?")
                                .setMessage("The project owner will review your request.")

                                .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        name.setText("Waiting for response");
                                    }
                                })

                                // dismiss the dialog and do nothing
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                }
            });
        }
    }
}
