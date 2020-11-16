package com.example.assembee

import ProjectListAdaptor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class search_project : AppCompatActivity() {
    private var title_list: ArrayList<String> = ArrayList()
    private var owners: ArrayList<String> = ArrayList()
    private var descriptions: ArrayList<String> = ArrayList()
    //private var results: JSONObject = JSONObject("")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_search_project)
        val projectName = intent.getStringExtra("ProjectName")
        Log.d("Add_Post", "the value is $projectName")
        //val details: JSONObject? = projectName?.let { sendGet(it) }
        
        val layout_manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView = findViewById<RecyclerView>(R.id.search_project_recycler_view)
        recyclerView?.layoutManager = layout_manager
        val search_list_adaptor: SearchListAdaptor = SearchListAdaptor(title_list, owners, descriptions, this);
        recyclerView?.adapter = search_list_adaptor

       // val details: JSONObject? = projectName?.let { sendGet(it) }

//        title_list.add("test")
//        owners.add("t est")
//        descriptions.add("TESTSTESTSETEST")
        //createPosts(details)
        Log.d("info", "got here")

    }

    fun sendGet(projectName: String): JSONObject?{
        Log.d("Add_Post 2", "the value is $projectName")
        var results: JSONObject? = null
        val cache = DiskBasedCache(cacheDir, 2048 * 2048) // 2MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        val queue = Volley.newRequestQueue(this)
        val url = "https://assembee.dissi.dev/search/$projectName"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                Log.d("info", "Response is: $response")
                results = JSONObject(response)
                //createPosts(results)
            },
            Response.ErrorListener { Log.d("info", "didn't work") })

        queue.add(stringRequest)
        return results
    }


    fun createPosts(results: JSONObject?){
        val project_list = results?.getJSONArray("projects")
        val lengthList: Int? = project_list?.length()
        if(results != null){
            for (i in 0 until lengthList!!) {
                title_list.add(project_list.getJSONObject(i).getString("name"))
                owners.add(project_list.getJSONObject(i).getJSONObject("owner").getString("name"))
                descriptions.add(project_list.getJSONObject(i).getString("description"))
            }
        }


        // Inflate the layout for this fragment


    }



}