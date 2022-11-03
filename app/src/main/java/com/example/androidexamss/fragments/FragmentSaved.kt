package com.example.androidexamss.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexamss.Globals
import com.example.androidexamss.dataAdapters.SavedMainAdapter
import com.example.androidexamss.R

class FragmentSaved : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_saved, container, false)
        Globals.imagePos = -1
        val historyRecyclerView: RecyclerView = view.findViewById(R.id.mainRcv)
        val itemAdapter = SavedMainAdapter()

        historyRecyclerView.adapter = itemAdapter

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSaved()
    }
}