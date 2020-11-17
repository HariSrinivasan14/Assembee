package com.example.assembee

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.awaitAll
import org.json.JSONObject
import kotlinx.coroutines.delay

class search_project : AppCompatActivity(), VolleyCallBack{
    private var title_list: ArrayList<String> = ArrayList()
    private var owners: ArrayList<String> = ArrayList()
    private var descriptions: ArrayList<String> = ArrayList()
//    //private var results: JSONObject = JSONObject("")
    override fun onResponse(result: String) {
        Log.d("info", "onResponse: "+result)
//        title_list.add(result)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val projectName = intent.getStringExtra("ProjectName")
        Log.d("info", "project name: "+projectName)
        val url = "https://assembee.dissi.dev/search/"+projectName
        MyVolleyRequest.getInstance(this@search_project, this@search_project)
                .getRequest(url)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_project)

//        title_list.add("test")
//        owners.add("t est")
//        descriptions.add("TESTSTESTSETEST")
//        title_list.add("test")
//        owners.add("t est")
//        descriptions.add("TESTSTESTSETEST")
//        title_list.add("test")
//        owners.add("t est")
//        descriptions.add("TESTSTESTSETEST")
        val layout_manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView = findViewById<RecyclerView>(R.id.search_project_recycler_view)
        recyclerView?.layoutManager = layout_manager
        val a = title_list
        val search_list_adaptor = SearchListAdaptor(title_list, owners, descriptions, this);
        recyclerView?.adapter = search_list_adaptor


//        val projectName = intent.getStringExtra("ProjectName")
//        Log.d("Add_Post", "the value is $projectName")
//        //val details: JSONObject? = projectName?.let { sendGet(it) }
////        val details: JSONObject? = projectName?.let { sendGet(it) }
//        projectName?.let { sendGet(it) }


//
//
//        //createPosts(details)
//        Log.d("info", "got here")
    }




//    fun sendGet(projectName: String): JSONObject?{
//        Log.d("Add_Post 2", "the value is $projectName")
//        var results: JSONObject? = null
//        //val cache = DiskBasedCache(cacheDir, 2048 * 2048) // 2MB cap
//
//        // Set up the network to use HttpURLConnection as the HTTP client.
////        val network = BasicNetwork(HurlStack())
//
//        // Instantiate the RequestQueue with the cache and network. Start the queue.
////        val requestQueue = RequestQueue(cache, network).apply {
////            start()
////        }
//
//        val queue = Volley.newRequestQueue(this)
//        val url = "https://assembee.dissi.dev/search/$projectName"
//
////        val stringRequest = StringRequest(
////                Request.Method.GET, url, Response.Listener<String>
////                { response ->
////                    // Display the first 500 characters of the response string.
////
////
////                    results = JSONObject(response)
////
////
////                },
////                { Log.d("info", "didn't work") })
//        val getRequest = JsonObjectRequest(Request.Method.GET, url,null,Response.Listener{response ->
//
//        })
////                Response.Listener<String> { response ->
////                    results = JSONObjeact(response)
////                    val project_list = results?.getJSONArray("projects")
////                    if (project_list != null) {
////                        title_list.add(project_list.getJSONObject(0).getString("name"))
////                        owners.add(project_list.getJSONObject(0).getJSONObject("owner").getString("name"))
////                        descriptions.add(project_list.getJSONObject(0).getString("description"))
////                    }
////
////                },
//                Response.ErrorListener { error ->
//                    // Handle error
//                    Log.d("info", "didn't work")
//                })

//        Log.d("info", "Response is a : $results")
//        createPosts(results)
//        queue.add(stringRequest)
//        return results
//    }
//
//
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
    }




}