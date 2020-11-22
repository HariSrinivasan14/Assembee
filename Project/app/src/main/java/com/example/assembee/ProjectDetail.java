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
import android.widget.LinearLayout;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectDetail extends AppCompatActivity {
    ArrayList<String> contributors;
    ArrayList<String> avatarUrls;
    ArrayList<String> userIds;
    AvatarListAdaptor avatarListAdaptor;
    String projectId;
    String ownerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contributors = new ArrayList<>();
        setContentView(R.layout.activity_project_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contributors = new ArrayList<>();
        avatarUrls = new ArrayList<>();
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
        setStatusEditListener(findViewById(R.id.editState), "state", R.id.state);

        // for tags
        setEditTagsListener(findViewById(R.id.editCatagroies), "categories", R.id.projectCategory);

        // get projectId intent
        Intent intent = getIntent();
        projectId = intent.getStringExtra("projectId");

        // set contributors's manager
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView avatar_recycler = findViewById(R.id.avatar_recyler_view);
        avatar_recycler.setLayoutManager(manager);
        avatarListAdaptor = new AvatarListAdaptor(contributors, userIds, avatarUrls, this);
        avatar_recycler.setAdapter(avatarListAdaptor);

        // setup the fab
        findViewById(R.id.join_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ProjectDetail.this)
                        .setTitle("Request to join this project?")
                        .setMessage("The project owner will review your request.")

                        .setPositiveButton("Join", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences
                                        = ProjectDetail.this.getSharedPreferences("sharedPref",
                                        MODE_PRIVATE);

                                String url = "https://assembee.dissi.dev/notifications";
                                JSONObject post_body = new JSONObject();
                                Log.d("post body", ""+userId+" "+ownerId +  " "+ projectId);
                                try {
                                    post_body = post_body.put("from", sharedPreferences.getString("userId", null))
                                            .put("to", ownerId)
                                            .put("project", projectId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                RequestQueue requstQueue = Volley.newRequestQueue(ProjectDetail.this);

                                JsonObjectRequest body = new JsonObjectRequest(Request.Method.POST, url, post_body,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // Storing userId into SharedPreferences
                                                ProjectDetail.this.contributors.add("Waiting for response");
                                                ProjectDetail.this.avatarListAdaptor.notifyDataSetChanged();

                                                // hide the join button
                                                findViewById(R.id.join_button).setVisibility(View.GONE);
                                                Toast.makeText(ProjectDetail.this, "The project owner will contact you if you are a good fit", Toast.LENGTH_LONG).show();
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
            }
        });


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
                        ChipGroup tags = findViewById(R.id.project_tags);

                        try {
                            // hide edit buttons if the use is not the owner
                            ownerId = response.getJSONObject("owner").getString("id");
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

                            // contributors
                            JSONArray c = response.getJSONArray("contributors");
                            Log.d("contributors list", c.toString());
                            for (int i = 0; i < c.length(); ++i) {
                                JSONObject user = c.getJSONObject(i);
                                userIds.add(user.getString("id"));
                                avatarUrls.add(user.getString("avatar"));
                                contributors.add(user.getString("name"));
                            }
                            avatarListAdaptor.notifyDataSetChanged();

                            // tags
                            JSONArray t = response.getJSONArray("categories");
                            for (int i = 0; i < t.length(); ++i) {
                                if (t.get(i).equals("Web")) {
                                    findViewById(R.id.web_tag).setVisibility(View.VISIBLE);
                                }
                                else if (t.get(i).equals("Android")) {
                                    findViewById(R.id.android_tag).setVisibility(View.VISIBLE);
                                }
                                else if (t.get(i).equals("iOS")) {
                                    findViewById(R.id.ios_tag).setVisibility(View.VISIBLE);
                                }
                                else if (t.get(i).equals("AI")) {
                                    findViewById(R.id.ai_tag).setVisibility(View.VISIBLE);
                                }
                                else {
                                    findViewById(R.id.other_tag).setVisibility(View.VISIBLE);
                                }
                            }
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

    private void setEditTagsListener(ImageButton button, String field, int groupId) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetail.this);
                builder.setTitle("Edit " + field);

                ChipGroup group = new ChipGroup(ProjectDetail.this);
                Chip web = new Chip(ProjectDetail.this);
                web.setCheckable(true);
                web.setChecked(true);
                web.setText("Web");
                if (findViewById(R.id.web_tag).getVisibility() != View.VISIBLE) {
                    web.setChecked(false);
                }
                group.addView(web, 0);

                Chip android = new Chip(ProjectDetail.this);
                android.setText("Android");
                android.setCheckable(true);
                android.setChecked(true);
                if (findViewById(R.id.android_tag).getVisibility() != View.VISIBLE) {
                    android.setChecked(false);
                }
                group.addView(android, 1);

                Chip ios = new Chip(ProjectDetail.this);
                ios.setText("iOS");
                ios.setCheckable(true);
                ios.setChecked(true);
                if (findViewById(R.id.ios_tag).getVisibility() != View.VISIBLE) {
                    ios.setChecked(false);
                }

                group.addView(ios, 2);

                Chip ai = new Chip(ProjectDetail.this);
                ai.setText("AI");
                ai.setCheckable(true);
                ai.setChecked(true);
                if (findViewById(R.id.ai_tag).getVisibility() != View.VISIBLE) {
                    ai.setChecked(false);
                }
                group.addView(ai, 3);

                Chip other = new Chip(ProjectDetail.this);
                other.setCheckable(true);
                other.setChecked(true);
                other.setText("Other");
                if (findViewById(R.id.other_tag).getVisibility() != View.VISIBLE) {
                    other.setChecked(false);
                }
                group.addView(other, 4);

                builder.setView(group);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        JSONArray patch_body = new JSONArray();
                        if (web.isChecked()) {
                            patch_body.put("Web");
                        }
                        if (android.isChecked()) {
                            patch_body.put("Android");
                        }
                        if (ios.isChecked()) {
                            patch_body.put("iOS");
                        }
                        if (ai.isChecked()) {
                            patch_body.put("AI");
                        }
                        if (other.isChecked()) {
                            patch_body.put("Other");
                        }

                        String url = "https://assembee.dissi.dev/project/" + projectId;
                        JsonObjectRequest req = null;
                        try {
                            req = new JsonObjectRequest(Request.Method.PATCH,
                                    url,
                                    new JSONObject().put("categories", patch_body),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("update BODY", response.toString());
                                            // tags
                                            JSONArray t = null;

                                            try {
                                                t = response.getJSONArray("categories");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            for (int i = 0; i < t.length(); ++i) {
                                                try {
                                                    if (t.get(i).equals("Web")) {
                                                        findViewById(R.id.web_tag).setVisibility(View.VISIBLE);
                                                    } else if (t.get(i).equals("Android")) {
                                                        findViewById(R.id.android_tag).setVisibility(View.VISIBLE);
                                                    } else if (t.get(i).equals("iOS")) {
                                                        findViewById(R.id.ios_tag).setVisibility(View.VISIBLE);
                                                    } else if (t.get(i).equals("AI")) {
                                                        findViewById(R.id.ai_tag).setVisibility(View.VISIBLE);
                                                    } else {
                                                        findViewById(R.id.other_tag).setVisibility(View.VISIBLE);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }                                        }
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

    private void setStatusEditListener(ImageButton button, String field, int id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://assembee.dissi.dev/project/" + projectId;
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetail.this);
                TextView currentState = findViewById(R.id.state);
                builder.setTitle("Edit " + field + ": " + currentState.getText().toString());

                String[] states = {"ongoing", "finished", "delete project"};
                builder.setItems(states, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                JsonObjectRequest req = null;
                                try {
                                    req = null;
                                    req = new JsonObjectRequest(Request.Method.PATCH,
                                            url,
                                            new JSONObject().put(field, "ongoing"),
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
                            case 1: {
                                JsonObjectRequest req = null;
                                try {
                                    req = null;
                                    req = new JsonObjectRequest(Request.Method.PATCH,
                                            url,
                                            new JSONObject().put(field, "finished"),
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
                            case 2: {
                                JsonObjectRequest req = null;
                                req = null;
                                req = new JsonObjectRequest(Request.Method.DELETE,
                                        url,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d("deleted", response.toString());
                                                Toast.makeText(ProjectDetail.this, "Project deleted", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d("Error", "Error: " + error.getMessage());
                                        Toast.makeText(ProjectDetail.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                                RequestQueue requstQueue = Volley.newRequestQueue(ProjectDetail.this);
                                requstQueue.add(req);
                            }
                        }
                    }
                });

                builder.show();
            }
        });
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