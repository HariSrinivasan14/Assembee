package com.example.assembee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

        // get projectId intent
        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
//        Log.d("got projectId", projectId);

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

                        try {
                            projectName.setText(response.getString("name"));
                            owner.setText(response.getJSONObject("owner").getString("name"));
                            desc.setText(response.getString("description"));
                            skills.setText(response.getString("skills"));
                            avail.setText(response.getString("availability"));
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

        // populate the contributor list
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