package com.example.assembee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import android.widget.ImageButton
import android.widget.RelativeLayout


/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class explore_dummy : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        val result = inflater.inflate(R.layout.fragment_explore_dummy, container, false)
        val searchView = result?.findViewById<SearchView>(R.id.search_view)
        Log.d("Add_Post", "the value is $searchView")
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val projectname: String? =  query
                val intent = Intent(activity, search_project :: class.java)
                intent.putExtra("ProjectName", projectname)

                startActivity(intent)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
        val IOS_btn = result?.findViewById<ImageButton>(R.id.IOS)
        val Android_btn = result?.findViewById<ImageButton>(R.id.Android_category)
        val Web_btn = result?.findViewById<ImageButton>(R.id.Web_category)
        val ML_btn = result?.findViewById<ImageButton>(R.id.ML_category)
        val Other_btn = result?.findViewById<ImageButton>(R.id.Other_category)
        if (IOS_btn != null) {
            IOS_btn.setOnClickListener{
                val intent = Intent(activity, search_project::class.java)
                val category = "IOS"
                intent.putExtra("category", category)
                startActivity(intent)
            }
        }
        if (Android_btn != null) {
            Android_btn.setOnClickListener{
                val intent = Intent(activity, search_project::class.java)
                val category = "Android"
                intent.putExtra("category", category)
                startActivity(intent)
            }
        }
        if (Web_btn != null) {
            Web_btn.setOnClickListener{
                val intent = Intent(activity, search_project::class.java)
                val category = "Web"
                intent.putExtra("category", category)
                startActivity(intent)
            }
        }
        if (ML_btn != null) {
            ML_btn.setOnClickListener{
                val intent = Intent(activity, search_project::class.java)
                val category = "ML"
                intent.putExtra("category", category)
                startActivity(intent)
            }
        }
        if (Other_btn != null) {
            Other_btn.setOnClickListener{
                val intent = Intent(activity, search_project::class.java)
                val category = "Other"
                intent.putExtra("category", category)
                startActivity(intent)
            }
        }
        return result
    }

    companion object {
        fun newInstance(): explore_dummy = explore_dummy()
    }
}