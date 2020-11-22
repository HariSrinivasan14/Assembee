package com.example.assembee

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject


class search_project : AppCompatActivity(), VolleyCallBack{

    private lateinit var title_list: ArrayList<String>
    private lateinit var owners: ArrayList<String>
    private lateinit var descriptions: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    private lateinit var search_list_adaptor: SearchListAdaptor
    private lateinit var layout_manager: LinearLayoutManager
    private lateinit var projectIds: ArrayList<String>

    override fun onResponse(result: JSONObject?) {

        createPosts(result)

        Log.d("info", "onResponse: " + result)
        search_list_adaptor.notifyDataSetChanged()
        var text: TextView = findViewById(R.id.search_text)
        text.setText(title_list[0])

//        Log.d("info", "onResponse: " + search_list_adaptor.gettitles().get(0))

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        title_list = ArrayList()
        owners = ArrayList()
        descriptions = ArrayList()
        projectIds = ArrayList()
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        setContentView(R.layout.activity_search_project)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(findViewById(R.id.search_bar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        val projectName = intent.getStringExtra("ProjectName")
//        val category = intent.getStringExtra("category")
//
//        var url = ""
//        if (category != null){
//            url = "https://assembee.dissi.dev/category/"+category
//        }
//        else{
//            url = "https://assembee.dissi.dev/search/"+projectName
//        }
//
//
//        layout_manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        search_list_adaptor = SearchListAdaptor(title_list, owners, descriptions, this);
//        recyclerView = findViewById(R.id.search_project_recycler_view)
//        Log.d("info", "recycle: " + recyclerView)
//
//        recyclerView?.layoutManager = layout_manager
//
//        MyVolleyRequest.getInstance(this@search_project, this@search_project)
//                .getRequest(url)
//        recyclerView?.adapter = search_list_adaptor
//        search_list_adaptor.notifyDataSetChanged()
//        var text: TextView = findViewById(R.id.search_text)
//        text.text = "123"

    }
    override fun onBackPressed(){
        super.onBackPressed()
//        val intent = Intent(this, MainActivity:: class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
        title_list.clear()
        Log.d("info", "title_list: " + title_list)
        owners.clear()
        descriptions.clear()



    }
    override fun onPause(){
        super.onPause()

        title_list.clear()
        Log.d("info", "title_list: " + title_list)
        owners.clear()
        descriptions.clear()
    }
    override fun onResume() {
        super.onResume()

        title_list.clear()
        owners.clear()
        descriptions.clear()
//        val txt = findViewById<EditText>(R.id.search_text).apply {
//            setText("123")
//        }
        val projectName = intent.getStringExtra("ProjectName")
        val category = intent.getStringExtra("category")

        Log.d("info", "Resume project name: " + projectName)
        var url = ""
        if (category != null){
            url = "https://assembee.dissi.dev/category/"+category
        }
        else{
            url = "https://assembee.dissi.dev/search/"+projectName
        }

        layout_manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        search_list_adaptor = SearchListAdaptor(title_list, owners, descriptions, projectIds,this);
//        Log.d("info", "resume: " + )
        recyclerView = findViewById<RecyclerView>(R.id.search_project_recycler_view)

        recyclerView?.layoutManager = layout_manager
        recyclerView?.adapter = search_list_adaptor


        MyVolleyRequest.getInstance(this@search_project, this@search_project)
            .getRequest(url)
        search_list_adaptor.notifyDataSetChanged()



    }
    private fun createPosts(results: JSONObject?){
        val project_list = results?.getJSONArray("projects")
        val lengthList: Int? = project_list?.length()
        if(results != null){
            for (i in 0 until lengthList!!) {
                title_list.add(project_list.getJSONObject(i).getString("name"))
                owners.add(project_list.getJSONObject(i).getJSONObject("owner").getString("name"))
                descriptions.add(project_list.getJSONObject(i).getString("description"))
                projectIds.add(project_list.getJSONObject(i).getString("id"))
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                //                Intent intent = new Intent(user_profile.this, MainActivity.class);
                //                startActivity(intent);
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}