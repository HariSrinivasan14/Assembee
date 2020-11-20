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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_list = ArrayList( )
        owners = ArrayList()
        descriptions = ArrayList()
        projectIds = ArrayList()
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
                        Log.d("Response", response.toString())
                        var owner_projects: JSONArray = response.getJSONArray("owner")
                        var cont_projects: JSONArray = response.getJSONArray("contributor")
                        for (i in 0 until owner_projects.length()) {
                            var project: JSONObject = owner_projects.getJSONObject(i)
                            title_list.add(project.getString("name"))
                            owners.add(project.getJSONObject("owner").getString("name"))
                            descriptions.add(project.getString("description"))
                            projectIds.add(project.getString("id"))
                        }

                        Log.d("length of lists", ""+title_list.size);

                        for (i in 0 until cont_projects.length()) {
                            // TODO
                        }

                        project_list_adaptor.notifyDataSetChanged()
                        Log.d("length of adaptor", ""+project_list_adaptor.itemCount)
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

        // fetch the projects of the user & re-render project list
        Log.w("item count", ""+project_list_adaptor.itemCount)

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
                        Log.d("Response", response.toString())
                        Log.d("responseID", response.getString("user_id"))
                        var owner_projects: JSONArray = response.getJSONArray("owner")
                        Log.w("owner_p", owner_projects.toString())
                        var cont_projects: JSONArray = response.getJSONArray("contributor")
                        Log.w("cont_p", cont_projects.toString())

                        for (i in 0 until owner_projects.length()) {
                            var project: JSONObject = owner_projects.getJSONObject(i)
                            title_list.add(project.getString("name"))
                            owners.add(project.getJSONObject("owner").getString("name"))
                            descriptions.add(project.getString("description"))
                            projectIds.add(project.getString("id"))
                        }
                        for (i in 0 until cont_projects.length()) {
                            // TODO
                        }

                        Log.d("length of lists", ""+title_list.size);
                        view?.findViewById<TextView>(R.id.notLoggedInLabel)?.visibility = View.GONE;
                        project_list_adaptor.notifyDataSetChanged()
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
        val sp: SharedPreferences? = activity?.getSharedPreferences(
            "sharedPref",
            Context.MODE_PRIVATE
        )
        var res = inflater.inflate(R.layout.fragment_dashboard, container, false)
        if(sp != null) {
            view?.findViewById<TextView>(R.id.notLoggedInLabel)?.visibility = View.GONE;
        }

        // set up adaptor
        val layout_manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        projectRecylerView = res?.findViewById(R.id.dash_project_recycler_view)!!
        projectRecylerView.layoutManager = layout_manager
        project_list_adaptor = ProjectListAdaptor(title_list, owners, descriptions, projectIds, context)
        projectRecylerView.adapter = project_list_adaptor

        return res
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}