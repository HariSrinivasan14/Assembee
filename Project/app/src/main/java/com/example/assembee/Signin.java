package com.example.assembee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// see here https://developers.google.com/identity/sign-in/android/sign-in

public class Signin extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    GoogleSignInClient mGoogleSignInClient;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if there's an account logged in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            setContentView(R.layout.activity_signin);
            Toolbar toolbar = findViewById(R.id.login_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        } else {
//            setContentView(R.layout.activity_user_profile);
//            Toolbar toolbar = findViewById(R.id.profile_toolbar);
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Intent intent = new Intent(this,user_profile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            JSONObject post_body = new JSONObject();
            post_body.put("name", account.getDisplayName())
                     .put("avatar", account.getPhotoUrl().toString())
                     .put("email", account.getEmail());

            String url = "https://assembee.dissi.dev/user";
            RequestQueue requstQueue = Volley.newRequestQueue(this);

            JsonObjectRequest body = new JsonObjectRequest(Request.Method.POST, url, post_body,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Storing userId into SharedPreferences
                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("sharedPref",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor edit
                                    = sharedPreferences.edit();

                            try {
                                edit.putString(
                                        "userId",
                                        response.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            edit.commit();

                            Log.w("resp", response.toString());
                            Intent intent = new Intent(Signin.this,user_profile.class);
                            intent.putExtra("is_user", true);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.w("volley", "error");
                        }
                    }
            ){
                //here I want to post data to sever
            };
            requstQueue.add(body);


//            setContentView(R.layout.activity_user_profile);
//            Toolbar toolbar = findViewById(R.id.profile_toolbar);
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (ApiException | JSONException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Snackbar.make(findViewById(R.id.login_toolbar), "Sign in failed :(, please try again", Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(Signin.this, MainActivity.class);
                startActivity(intent);
//                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}