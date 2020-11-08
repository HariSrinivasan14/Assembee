package com.example.assembee

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class Add_Post : AppCompatActivity() {


    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post)
//        setSupportActionBar(findViewById(R.id.toolbar))
//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        val button_cancel: ImageButton = findViewById(R.id.cancel_button)
        button_cancel.setOnClickListener {

            onBackPressed()
        }
        val button_addP: Button = findViewById(R.id.postButton)
        val projectName: TextView = findViewById(R.id.textProjectName)
        val contactInfo: TextView = findViewById(R.id.textContactInformation)
        val availability: TextView = findViewById(R.id.textPreferredAvailability)
        val dd: TextView = findViewById(R.id.textPreferredAvailability)
        button_addP.setOnClickListener{
            val str: String =  projectName.text.toString()
            val str2: String = contactInfo.text.toString()
            val str3: String = availability.text.toString()
            val str4: String = dd.text.toString()
            Log.d("Add_Post", "the value is $str")
            println("the value is $str")
//            val intent = Intent(this, DashboardFragment::class.java)
//            intent.putExtra("ProjectName", str)
//            intent.putExtra("contactInfo", str2)
//            intent.putExtra("availabilityInfo", str3)
//            intent.putExtra("dd", str4)
//            startActivity(intent)

            // shared preferences
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString("ProjectName", str)
            editor.putString("contactInfo", str2)
            editor.putString("availabilityInfo", str3)
            editor.putString("dd", str4)
            editor.apply()
            editor.commit()

            val intent = Intent(this, MainActivity :: class.java)
            startActivity(intent)

        }
    }

}