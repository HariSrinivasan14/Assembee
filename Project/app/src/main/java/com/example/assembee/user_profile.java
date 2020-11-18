package com.example.assembee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
                    TextView name = findViewById(R.id.profile_name);
                    try {
                        name.setText(response.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CircularImageView avatar = findViewById(R.id.user_avatar);
                    try {
                        Glide.with(user_profile.this)
                                .load(response.getString("avatar"))
                                .into(avatar);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            // hide the signout button
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            // display the name from intent
            username = getIntent().getStringExtra("name");
            TextView name = findViewById(R.id.profile_name);
            name.setText(username);

        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(user_profile.this, "Logged out", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(user_profile.this, MainActivity.class);
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