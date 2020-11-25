package com.example.assembee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class user_profile extends AppCompatActivity {
    String username;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean is_user = getIntent().getBooleanExtra("is user", true);
        if (is_user) {
            // set up all the edit buttons
            setEditListener(findViewById(R.id.editIntro), "bio", R.id.intro);
            setEditListener(findViewById(R.id.editSkills), "skills", R.id.skills);
            setEditListener(findViewById(R.id.editAvail), "availability", R.id.avail);
            setEditListener(findViewById(R.id.editContacts), "contacts", R.id.contacts);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            // get saved userId
            SharedPreferences sh
                    = getSharedPreferences("sharedPref",
                    MODE_PRIVATE);

            String userId = sh.getString("userId", "");

            // fetch the user profile via api
            String url = "https://assembee.dissi.dev/user/" + userId;
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                    CircularImageView avatar = findViewById(R.id.user_avatar);
                    TextView bio = findViewById(R.id.intro);
                    TextView contacts = findViewById(R.id.contacts);
                    TextView avail = findViewById(R.id.avail);
                    TextView skills = findViewById(R.id.skills);

                    TextView name = findViewById(R.id.profile_name);
                    try {
                        name.setText(response.getString("name"));
                        Glide.with(user_profile.this)
                                .load(response.getString("avatar"))
                                .into(avatar);
                        bio.setText(response.getString("bio"));
                        contacts.setText(response.getString("contacts"));
                        avail.setText(response.getString("availability"));
                        skills.setText(response.getString("skills"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // sign out button; only for user logged in
                    findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signOut();
                        }
                    });

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", "Error: " + error.getMessage());
                    Toast.makeText(user_profile.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            RequestQueue requstQueue = Volley.newRequestQueue(this);
            requstQueue.add(req);

        } else {
            // hide all edit buttons for non-user
            findViewById(R.id.editIntro).setVisibility(View.GONE);
            findViewById(R.id.editContacts).setVisibility(View.GONE);
            findViewById(R.id.editSkills).setVisibility(View.GONE);
            findViewById(R.id.editAvail).setVisibility(View.GONE);

            // hide the signout button
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            // display the user from intent
            String userId = getIntent().getStringExtra("userId");
            // fetch the user profile via api
            String url = "https://assembee.dissi.dev/user/" + userId;
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                    CircularImageView avatar = findViewById(R.id.user_avatar);
                    TextView bio = findViewById(R.id.intro);
                    TextView contacts = findViewById(R.id.contacts);
                    TextView avail = findViewById(R.id.avail);
                    TextView skills = findViewById(R.id.skills);

                    TextView name = findViewById(R.id.profile_name);
                    try {
                        name.setText(response.getString("name"));
                        Glide.with(user_profile.this)
                                .load(response.getString("avatar"))
                                .into(avatar);
                        bio.setText(response.getString("bio"));
                        contacts.setText(response.getString("contacts"));
                        avail.setText(response.getString("availability"));
                        skills.setText(response.getString("skills"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", "Error: " + error.getMessage());
                    Toast.makeText(user_profile.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            RequestQueue requstQueue = Volley.newRequestQueue(this);
            requstQueue.add(req);
        }
    }

    private void setEditListener(ImageButton button, String field, int id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(user_profile.this);
                builder.setTitle("Edit " + field);

                EditText input = new EditText(user_profile.this);
                TextView current = findViewById(id);
                input.setText(current.getText());
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Edited", input.getText().toString());
                        // get saved userId
                        SharedPreferences sh

                                = getSharedPreferences("sharedPref",
                                MODE_PRIVATE);

                        String userId = sh.getString("userId", "");

                        // fetch the user profile via api
                        String url = "https://assembee.dissi.dev/user/" + userId;
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
                                    Toast.makeText(user_profile.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestQueue requstQueue = Volley.newRequestQueue(user_profile.this);
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
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(user_profile.this, "Logged out", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(user_profile.this, MainActivity.class);
                        // clear all the info in shared pref
                        SharedPreferences sh
                                = getSharedPreferences("sharedPref",
                                MODE_PRIVATE);

                        sh.edit().clear().apply();

                        startActivity(intent);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent = new Intent(user_profile.this, MainActivity.class);
//                startActivity(intent);
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}