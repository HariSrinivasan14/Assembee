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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AvatarListAdaptor extends RecyclerView.Adapter<AvatarListAdaptor.ViewHolder> {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> userIds = new ArrayList<>();
    private Context context;

    public AvatarListAdaptor(ArrayList<String> names, ArrayList<String> userIds, Context context) {
        this.names = names;
        this.userIds = userIds;
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
                                        SharedPreferences sharedPreferences
                                                = context.getSharedPreferences("sharedPref",
                                                MODE_PRIVATE);

                                        String url = "https://assembee.dissi.dev/notifications";
                                        JSONObject post_body = new JSONObject();
                                        try {
                                            post_body = post_body.put("from", sharedPreferences.getString("userId", null))
                                                    .put("to", "")
                                                    .put("project", "");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        RequestQueue requstQueue = Volley.newRequestQueue(context);

                                        JsonObjectRequest body = new JsonObjectRequest(Request.Method.POST, url, post_body,
                                                new Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        // Storing userId into SharedPreferences
                                                        Log.w("join resp", response.toString());
                                                        name.setText("Waiting for response");
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.w("volley", "error");
                                                    }
                                                }
                                        ) {
                                        };
                                        requstQueue.add(body);
                                    }
                                })

                                // dismiss the dialog and do nothing
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    } else {
                        // open up a profile activity
                        Intent intent = new Intent(itemView.getContext(), user_profile.class);
                        // this bool indicates not to display logout & logged-in user username etc.
                        intent.putExtra("is user", false);
                        intent.putExtra("userId", userIds.get(getAdapterPosition()));
//                        intent.putExtra("name", names.get(getAdapterPosition()));

                        context.startActivity(intent);

                    }
                }
            });
        }
    }
}
