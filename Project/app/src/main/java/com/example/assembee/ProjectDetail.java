package com.example.assembee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ProjectDetail extends AppCompatActivity {

    int project_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        // for an action bar with a back button
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String project_id = intent.getStringExtra("project_id");
        Log.w("project detail got", project_id);

    }
}