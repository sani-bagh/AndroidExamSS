package com.example.androidexamss.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.androidexamss.DbController
import com.example.androidexamss.Globals
import com.example.androidexamss.R
import com.example.androidexamss.fragments.SearchFragment
import com.example.androidexamss.fragments.FragmentSaved
import com.example.androidexamss.fragments.FragmentUpload

class SearchActivity : AppCompatActivity() {
    //lateinit var binding: ActivitySearchBinding
    private var fragmentManager: FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_search)

        Globals.fragmentManager = supportFragmentManager
        DbController.initDb(this)

        /*
        binding.btnUpload.setOnClickListener {
            supportFragmentManager
                .beginTransaction().replace(R.id.placeholder, FragmentUpload.newInstance()).commit()
        }

        binding.btnReverse.setOnClickListener {
            supportFragmentManager
                .beginTransaction().replace(R.id.placeholder, SearchFragment.newInstance()).commit()
        }

        binding.btnF2.setOnClickListener {
            supportFragmentManager
                .beginTransaction().replace(R.id.placeholder, FragmentHistory.newInstance()).commit()
        }

         */
        fragmentManager = supportFragmentManager
        if (Globals.isFullSized == true) {
            fragmentManager.beginTransaction()
                .replace(R.id.placeholder, SearchFragment.newInstance()).commit()
        }else {
            fragmentManager.beginTransaction().replace(R.id.placeholder, FragmentUpload.newInstance()).commit()
        }
    }



    fun switchFragment(view: View) {
        fragmentManager = supportFragmentManager

        var buttonTag = Integer.parseInt(view.getTag().toString())

        if (buttonTag == 1) {
            fragmentManager.beginTransaction()
                .replace(R.id.placeholder, FragmentUpload.newInstance()).commit()
        }else if (buttonTag == 2) {
            fragmentManager.beginTransaction()
                .replace(R.id.placeholder, SearchFragment.newInstance()).commit()
        }else{
            fragmentManager.beginTransaction()
                .replace(R.id.placeholder, FragmentSaved.newInstance()).commit()
        }
    }


}