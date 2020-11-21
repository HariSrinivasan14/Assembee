package com.example.assembee

import Project
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
//import com.android.volley.Request
//import com.android.volley.VolleyError
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.Volley
import com.google.android.material.chip.Chip
import org.json.JSONArray
import org.json.JSONObject


class Add_Post : AppCompatActivity() {


    private val sharedPrefFile = "sharedPref"

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post)
//        setSupportActionBar(findViewById(R.id.toolbar))
//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        val button_cancel: ImageButton = findViewById(R.id.cancel_button)
        button_cancel.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("You are about to go back")
            alertDialog.setMessage("Are you sure you want to discard the project draft?")

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"
            ) { dialog, which -> dialog.dismiss()
                onBackPressed()

            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()

            val buttonPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val buttonNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = buttonPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            layoutParams.leftMargin = 10
            layoutParams.rightMargin = 30
            buttonPositive.layoutParams = layoutParams
            buttonNegative.layoutParams = layoutParams
        }
        val button_addP: Button = findViewById(R.id.postButton)
        val projectName: TextView = findViewById(R.id.textProjectName)
        val contactInfo: TextView = findViewById(R.id.textContactInformation)
        val availability: TextView = findViewById(R.id.textPreferredAvailability)
        val dd: TextView = findViewById(R.id.TextDD)
        val desiredskills: TextView = findViewById(R.id.TextDesiredSkills)
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
        desiredskills.setOnTouchListener(View.OnTouchListener { v, event ->
            if (v.id === R.id.TextDesiredSkills) {
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
            val str5: String = desiredskills.text.toString()
            var chip_array = get_chips().toCollection(ArrayList())
            val JA = JSONArray(chip_array)
            var new_post: JSONObject = JSONObject()
            new_post.put("name", str)
            new_post.put("description", str4)
            new_post.put("availability", str3)
            new_post.put("categories", JA)
            new_post.put("skills", str5)
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
            val temp: String? = sharedPreferences.getString("userId", null)
            new_post.put("owner", temp)
            //Post request

            val url = "https://assembee.dissi.dev/project"
            val requstQueue = Volley.newRequestQueue(this)

            val body: JsonObjectRequest =
                object : JsonObjectRequest(
                    Request.Method.POST, url, new_post,
                    Response.Listener<JSONObject?> {
                        fun onResponse(response: JSONObject) {

                            Log.w("resp", response.toString())

                        }
                    },
                    object : Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            Log.w("volley", "error")
                        }
                    }
                ) {}
            requstQueue.add(body)
            onBackPressed()

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
            checkedChips.add("Android")
        }
        if(chip3.isChecked){
            checkedChips.add("iOS")
        }
        if(chip4.isChecked){
            checkedChips.add("Machine Learning")
        }
        if(chip5.isChecked){
            checkedChips.add("AI")
        }
        if(chip6.isChecked){
            checkedChips.add("Other")
        }
        return checkedChips
    }

}