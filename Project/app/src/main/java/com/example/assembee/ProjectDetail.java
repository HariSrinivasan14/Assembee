package com.example.assembee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectDetail extends AppCompatActivity {
    ArrayList<String> contributors;
    ArrayList<String> userIds;
    AvatarListAdaptor avatarListAdaptor;
    String projectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contributors = new ArrayList<>();
        setContentView(R.layout.activity_project_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contributors = new ArrayList<>();
        userIds = new ArrayList<>();

        // get saved userId
        SharedPreferences sh
                = getSharedPreferences("sharedPref",
                MODE_PRIVATE);

        String userId = sh.getString("userId", "");

        // set up all the edit buttons for textviews
        setEditListener(findViewById(R.id.editTitle), "name", R.id.ProjectName);
        setEditListener(findViewById(R.id.editDesc), "description", R.id.ProjectDescription);
        setEditListener(findViewById(R.id.editskill), "skills", R.id.DesiredSkills);
        setEditListener(findViewById(R.id.editAvai), "availability", R.id.availbility);

        // get projectId intent
        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");

        // set contributors's manager
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView avatar_recycler = findViewById(R.id.avatar_recyler_view);
        avatar_recycler.setLayoutManager(manager);
        avatarListAdaptor = new AvatarListAdaptor(contributors, userIds, this);
        avatar_recycler.setAdapter(avatarListAdaptor);

        // fetch projects from api
        String url = "https://assembee.dissi.dev/project/" + projectId;
        RequestQueue queue = Volley.newRequestQueue(this);

        Request req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        TextView projectName = findViewById(R.id.ProjectName);
                        TextView owner = findViewById(R.id.ProjectOwner);
                        TextView desc = findViewById(R.id.ProjectDescription);
                        TextView skills = findViewById(R.id.DesiredSkills);
                        TextView avail = findViewById(R.id.availbility);
                        TextView state = findViewById(R.id.state);

                        try {
                            // hide edit buttons if the use is not the owner
                            if (!response.getJSONObject("owner").getString("id").equals(userId)) {
                                findViewById(R.id.editTitle).setVisibility(View.GONE);
                                findViewById(R.id.editState).setVisibility(View.GONE);
                                findViewById(R.id.editCatagroies).setVisibility(View.GONE);
                                findViewById(R.id.editDesc).setVisibility(View.GONE);
                                findViewById(R.id.editskill).setVisibility(View.GONE);
                                findViewById(R.id.editAvai).setVisibility(View.GONE);
                            }
                            projectName.setText(response.getString("name"));
                            owner.setText(response.getJSONObject("owner").getString("name"));
                            desc.setText(response.getString("description"));
                            skills.setText(response.getString("skills"));
                            avail.setText(response.getString("availability"));
                            state.setText(response.getString("status"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });
        queue.add(req);
    }
    private void setEditListener(ImageButton button, String field, int id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetail.this);
                builder.setTitle("Edit " + field);

                EditText input = new EditText(ProjectDetail.this);
                TextView current = findViewById(id);
                input.setText(current.getText());
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://assembee.dissi.dev/project/" + projectId;
                        JsonObjectRequest req = null;
                        try {
                            req = new JsonObjectRequest(Request.Method.PATCH,
                                    url,
                                    new JSONObject().put(field, input.getText().toString()),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // update field upon resonse
                                            TextView current = findViewById(id);
                                            try {
                                                current.setText(response.getString(field));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.d("Error", "Error: " + error.getMessage());
                                    Toast.makeText(ProjectDetail.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestQueue requstQueue = Volley.newRequestQueue(ProjectDetail.this);
                        requstQueue.add(req);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}