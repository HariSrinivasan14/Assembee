package com.example.assembee

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast

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
        val searchView = view?.findViewById<SearchView>(R.id.search_view)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val projectname: String? =  query
                val intent = Intent(activity, search_project::class.java)
                intent.putExtra("ProjectName", projectname)
                startActivity(intent)
                return true
            }

            //            override fun onQueryTextSubmit(query: String): Boolean {
//                if (list.contains(query)) {
//                    adapter.filter.filter(query)
//                } else {
//                    Toast.makeText(this@MainActivity, "No Match found", Toast.LENGTH_LONG).show()
//                }
//                return false
//            }
            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })
        return result
    }

    companion object {
        fun newInstance(): explore_dummy = explore_dummy()
    }
}