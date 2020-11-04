package com.example.assembee;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectDetail extends AppCompatActivity {
    int project_id = -1;
    String title;
    String owner;
    String description;
    ArrayList<String> contributors = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_project_detail);
        // for an action bar with a back button
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        owner = intent.getStringExtra("owner");
        description = intent.getStringExtra("description");
        TextView project_name = findViewById(R.id.ProjectName);
        TextView project_owner = findViewById(R.id.ProjectOwner);
        TextView project_description = findViewById(R.id.ProjectDescription);

        project_name.setText(title);
        project_owner.setText(owner);
        project_description.setText(description);


        // set contributors's manager
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView avatar_recycler = findViewById(R.id.avatar_recyler_view);
        avatar_recycler.setLayoutManager(manager);
        AvatarListAdaptor adaptor = new AvatarListAdaptor(contributors, this);
        avatar_recycler.setAdapter(adaptor);

        // populate the contributor list
        contributors.add("David");
        contributors.add("Elon");
        contributors.add("Jeff");
        contributors.add("Satya");
        contributors.add("Bill");
        contributors.add("Becky");


    }
}