package com.example.assembee

import ProjectListAdaptor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
//    private lateinit var recyclerView: RecyclerView?
//    private lateinit var viewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager
    private var title_list: ArrayList<String> = ArrayList()
    private var owners: ArrayList<String> = ArrayList()
    private var descriptions: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title_list = ArrayList()
        owners = ArrayList()
        descriptions = ArrayList()
    }
    private fun initRecylerView() {
        val layout_manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.dash_project_recycler_view)
        Log.w("is recyler null", (recyclerView?.findViewById<RecyclerView>(R.id.dash_project_recycler_view)==null).toString())
        recyclerView?.layoutManager = layout_manager
        val project_list_adaptor: ProjectListAdaptor = ProjectListAdaptor(title_list, owners, descriptions, activity);
        recyclerView?.adapter = project_list_adaptor
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        title_list.add("hehehe")
        owners.add("Elon")
        descriptions.add("looooooooooooooooooooooooooool")

        val exp_json = "{\n" +
                "    \"user_id\": \"4k1TaKukA65kR99lpRSV\",\n" +
                "    \"projects\": [\n" +
                "        {\n" +
                "            \"project_id\": \"lwoiqR7wrLtPPHpmRkir\",\n" +
                "            \"name\": \"Facebook\",\n" +
                "            \"description\": \"A social networking platform.\",\n" +
                "            \"owner\": {\n" +
                "                \"email\": \"david@example.com\",\n" +
                "                \"lastname\": \"Chen\",\n" +
                "                \"firstname\": \"David\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}"
        val user: JSONObject = JSONObject(exp_json)
        val project_list = user.getJSONArray("projects")
        for (i in 0 until project_list.length()) {
            title_list.add(project_list.getJSONObject(i).getString("name"))
            owners.add(project_list.getJSONObject(i).getJSONObject("owner").getString("firstname") + " " +
                    project_list.getJSONObject(i).getJSONObject("owner").getString("lastname"))
            descriptions.add(project_list.getJSONObject(i).getString("description"))
        }

        val res = inflater.inflate(R.layout.fragment_dashboard, container, false)
        // Inflate the layout for this fragment
        val layout_manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val recyclerView = res?.findViewById<RecyclerView>(R.id.dash_project_recycler_view)
        recyclerView?.layoutManager = layout_manager
        val project_list_adaptor: ProjectListAdaptor = ProjectListAdaptor(title_list, owners, descriptions, activity);
        recyclerView?.adapter = project_list_adaptor

        return res
    }

    companion object {
        fun newInstance(): DashboardFragment = DashboardFragment()
    }
}