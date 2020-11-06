package com.example.assembee

import android.os.Bundle
import android.widget.ImageButton
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class Add_Post : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post)
//        setSupportActionBar(findViewById(R.id.toolbar))
//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        val button_cancel: ImageButton = findViewById(R.id.cancel_button)
        button_cancel.setOnClickListener {
            onBackPressed()
        }
    }

}