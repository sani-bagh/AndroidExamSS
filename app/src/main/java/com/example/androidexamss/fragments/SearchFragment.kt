package com.example.androidexamss.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexamss.Globals
import com.example.androidexamss.dataAdapters.ImageItemAdapter
import com.example.androidexamss.R


class SearchFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val recView: RecyclerView = view.findViewById(R.id.rsRecyclerView)

        val imageItemAdapter = ImageItemAdapter()
        val loadingText: TextView? = view.findViewById(R.id.loadingText)

        recView.adapter = imageItemAdapter

        if (Globals.isLoading) {
            loadingText?.text = "Loading...."
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}