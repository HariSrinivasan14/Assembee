package com.example.assembee

import Project
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Scroller
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import java.util.*


class Add_Post : AppCompatActivity() {


    private val sharedPrefFile = "kotlinsharedpreference"

    @SuppressLint("ClickableViewAccessibility")
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
        val dd: TextView = findViewById(R.id.TextDD)
        contactInfo.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id === R.id.textContactInformation) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        availability.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id === R.id.textPreferredAvailability) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        dd.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id === R.id.TextDD) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        })
        button_addP.setOnClickListener{
            val str: String =  projectName.text.toString()
            val str2: String = contactInfo.text.toString()
            val str3: String = availability.text.toString()
            val str4: String = dd.text.toString()
            var chip_array = get_chips()
            var new_post = Project()
            new_post.setProjectName(str)
            new_post.setContactInfo(str2)
            new_post.setPreferredAvailability(str3)
            new_post.setDescription(str4)
            new_post.setCategories(get_chips() as ArrayList<String>?)
            Log.d("Add_Post", "the value is ${new_post.categories}")
            println("the value is $str")

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
    fun get_chips() : MutableList<String> {
        val chip1: Chip = findViewById(R.id.chip_1)
        val chip2: Chip = findViewById(R.id.chip_2)
        val chip3: Chip = findViewById(R.id.chip_3)
        val chip4: Chip = findViewById(R.id.chip_4)
        val chip5: Chip = findViewById(R.id.chip_5)
        val chip6: Chip = findViewById(R.id.chip_6)
        var checkedChips= mutableListOf<String>()
        if(chip1.isChecked){
            checkedChips.add("Web")
        }
        if(chip2.isChecked){
            checkedChips.add("Android_App")
        }
        if(chip3.isChecked){
            checkedChips.add("IOS_app")
        }
        if(chip4.isChecked){
            checkedChips.add("Machine_Learning")
        }
        if(chip5.isChecked){
            checkedChips.add("AI")
        }
        if(chip6.isChecked){
            checkedChips.add("Other")
        }
        return checkedChips
    }
//    fun createPost(new_post: JSONObject){
//
//    }

}