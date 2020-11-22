package com.example.assembee;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AvatarListAdaptor extends RecyclerView.Adapter<AvatarListAdaptor.ViewHolder> {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> avatars = new ArrayList<>();
    private ArrayList<String> userIds = new ArrayList<>();
    private Context context;

    public AvatarListAdaptor(ArrayList<String> names, ArrayList<String> userIds, ArrayList<String> avatars, Context context) {
        this.names = names;
        this.userIds = userIds;
        this.avatars = avatars;
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
        Glide.with(context)
                .load(avatars.get(position))
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CircularImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contributor_name);
            avatar = itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        // open up a profile activity
                        Intent intent = new Intent(itemView.getContext(), user_profile.class);
                        // this bool indicates not to display logout & logged-in user username etc.
                        intent.putExtra("is user", false);
                        intent.putExtra("userId", userIds.get(getAdapterPosition()));
//                        intent.putExtra("name", names.get(getAdapterPosition()));

                        context.startActivity(intent);

                }
            });
        }
    }
}
