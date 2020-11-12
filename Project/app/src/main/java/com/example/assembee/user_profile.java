package com.example.assembee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

public class user_profile extends AppCompatActivity {
    String username;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if whether calling from profile button or elsewhere TODO

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the user attributes from an intent
        username = GoogleSignIn.getLastSignedInAccount(this).getDisplayName();
//        username = intent.getStringExtra("name");
        TextView name = findViewById(R.id.profile_name);
        name.setText(username);
        findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
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