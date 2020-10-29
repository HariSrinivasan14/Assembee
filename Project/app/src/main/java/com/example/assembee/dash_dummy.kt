package com.example.assembee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [dash_dummy.newInstance] factory method to
 * create an instance of this fragment.
 */
class dash_dummy : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash_dummy, container, false)
    }

    companion object {
        fun newInstance(): dash_dummy = dash_dummy()
    }
}