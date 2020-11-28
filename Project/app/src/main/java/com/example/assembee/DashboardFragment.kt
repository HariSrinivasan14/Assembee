package com.example.assembee

import ProjectListAdaptor
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.mikhaellopez.circularimageview.CircularImageView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    private lateinit var title_list: ArrayList<String>
    private lateinit var owners: ArrayList<String>
    private lateinit var descriptions: ArrayList<String>
    private lateinit var projectIds: ArrayList<String>
    private lateinit var projectRecylerView: RecyclerView
    private lateinit var project_list_adaptor: ProjectListAdaptor

    private lateinit var pending_request_title: ArrayList<String>
    private lateinit var pending_request_ids: ArrayList<String>
    private lateinit var pending_request_owners: ArrayList<String>
    private lateinit var pending_request_descriptions: ArrayList<String>
    private lateinit var pending_requestRecyclerView: RecyclerView
    private lateinit var pending_request_adaptor: ProjectListAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_list = ArrayList( )
        owners = ArrayList()
        descriptions = ArrayList()
        projectIds = ArrayList()

        pending_request_title = ArrayList()
        pending_request_owners = ArrayList()
        pending_request_descriptions = ArrayList()
        pending_request_ids = ArrayList()

        val sp: SharedPreferences? = activity?.getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )

        if (sp != null) {
            // fetch projects from api
            if (sp.getString("userId", null) != null) {
                val url: String = "https://assembee.dissi.dev/projects/" +
                        (sp.getString("userId", null));

                val queue = Volley.newRequestQueue(context)

                val req = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener { response ->
                        // clear all lists
                        title_list.clear()
                        owners.clear()
                        descriptions.clear()
                        projectIds.clear()

                        pending_request_title.clear()
                        pending_request_owners.clear()
                        pending_request_descriptions.clear()
                        pending_request_ids.clear()

                        var owner_projects: JSONArray = response.getJSONArray("owner")
                        var cont_projects: JSONArray = response.getJSONArray("contributor")
                        for (i in 0 until owner_projects.length()) {
                            var project: JSONObject = owner_projects.getJSONObject(i)
                            title_list.add(project.getString("name"))
                            owners.add(project.getJSONObject("owner").getString("name"))
                            descriptions.add(project.getString("description"))
                            projectIds.add(project.getString("id"))
                        }

                        for (i in 0 until cont_projects.length()) {
                            var project: JSONObject = owner_projects.getJSONObject(i)
                            pending_request_title.add(project.getString("name"))
                            pending_request_owners.add(project.getJSONObject("owner").getString("name"))
                            pending_request_descriptions.add(project.getString("description"))
                            pending_request_ids.add(project.getString("id"))

                        }

                        project_list_adaptor.notifyDataSetChanged()
                        pending_request_adaptor.notifyDataSetChanged()
                        view?.findViewById<TextView>(R.id.notLoggedInLabel)?.visibility = View.GONE;

                    },
                    Response.ErrorListener { error ->
                        Log.w("dash error", error)
                    })
                queue.add(req)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // clear all lists
        title_list.clear()
        owners.clear()
        descriptions.clear()
        projectIds.clear()

        pending_request_title.clear()
        pending_request_owners.clear()
        pending_request_descriptions.clear()
        pending_request_ids.clear()

        val sp: SharedPreferences? = activity?.getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )

        if(sp != null) {
            if (sp.getString("userId", null) != null) {
                // fetch projects from api
                val url: String = "https://assembee.dissi.dev/projects/" +
                        (sp.getString("userId", null));
                val queue = Volley.newRequestQueue(context)

                val req = JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    Response.Listener { response ->
                        // clear all lists
                        title_list.clear()
                        owners.clear()
                        descriptions.clear()
                        projectIds.clear()

                        pending_request_title.clear()
                        pending_request_owners.clear()
                        pending_request_descriptions.clear()
                        pending_request_ids.clear()

                        var owner_projects: JSONArray = response.getJSONArray("owner")
                        var cont_projects: JSONArray = response.getJSONArray("contributor")

                        for (i in 0 until owner_projects.length()) {
                            var project: JSONObject = owner_projects.getJSONObject(i)
                            title_list.add(project.getString("name"))
                            owners.add(project.getJSONObject("owner").getString("name"))
                            descriptions.add(project.getString("description"))
                            projectIds.add(project.getString("id"))
                        }
                        for (i in 0 until cont_projects.length()) {
                            var project: JSONObject = cont_projects.getJSONObject(i)
                            pending_request_title.add(project.getString("name"))
                            pending_request_owners.add(project.getJSONObject("owner").getString("name"))
                            pending_request_descriptions.add(project.getString("description"))
                            pending_request_ids.add(project.getString("id"))
                        }

                        view?.findViewById<TextView>(R.id.notLoggedInLabel)?.visibility = View.GONE;
                        project_list_adaptor.notifyDataSetChanged()
                        pending_request_adaptor.notifyDataSetChanged()

                    },
                    Response.ErrorListener { error ->
                        Log.w("dash error", error)
                    })
                queue.add(req)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        // clear all lists
        title_list.clear()
        owners.clear()
        descriptions.clear()
        projectIds.clear()

        pending_request_title.clear()
        pending_request_owners.clear()
        pending_request_descriptions.clear()
        pending_request_ids.clear()


        val sp: SharedPreferences? = activity?.getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )

        var res = inflater.inflate(R.layout.fragment_dashboard, container, false)
        if(sp != null) {
            view?.findViewById<TextView>(R.id.notLoggedInLabel)?.visibility = View.GONE;
        }

        // set up adaptors
        val layout_manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        projectRecylerView = res?.findViewById(R.id.dash_project_recycler_view)!!
        projectRecylerView.layoutManager = layout_manager
        project_list_adaptor = ProjectListAdaptor(title_list, owners, descriptions, projectIds, context)
        projectRecylerView.adapter = project_list_adaptor

        val request_layout_manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        pending_requestRecyclerView = res?.findViewById(R.id.pendingRecyclerView)!!
        pending_requestRecyclerView.layoutManager = request_layout_manager
        pending_request_adaptor = ProjectListAdaptor(pending_request_title, pending_request_owners, pending_request_descriptions, pending_request_ids, context)
        pending_requestRecyclerView.adapter = pending_request_adaptor

        return res
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}