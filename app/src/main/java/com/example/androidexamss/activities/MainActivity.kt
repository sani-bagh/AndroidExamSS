package com.example.androidexamss.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidexamss.Globals
import com.example.androidexamss.R
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var cameraBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvText1: TextView = findViewById(R.id.textView1)

        val btnStart: Button = findViewById(R.id.btn_start)
        val btnMap: Button = findViewById(R.id.btn_map)

        getCamera()

        btnStart.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        btnMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        thread {
            Thread.sleep(1500)
            this.runOnUiThread(Runnable { tvText1.setText("Hello, I am Reverso the image search app! " + "Press start to begin.") })
        }




    }

    fun getCamera() {
        cameraBtn = findViewById(R.id.btn_camera)
        cameraBtn.setOnClickListener {
            var camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResFromCam.launch(camIntent)
        }
    }

    var startForResFromCam = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            var camBitmap = it.data?.extras?.get("data") as Bitmap

            Globals.imageBitmap = camBitmap

        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(Globals.TAG, "Activity 1 onStart")
        Toast.makeText(this, "Activity onStart", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Log.i(Globals.TAG, "Activity 1 onResume")
        Toast.makeText(this, "Activity onResume", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Log.i(Globals.TAG, "Activity 1 onPause")
        Toast.makeText(this, "Activity onPause", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Log.i(Globals.TAG, "Activity 1 onStop")
        Toast.makeText(this, "Activity onStop", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(Globals.TAG, "Activity 1 onRestart")
        Toast.makeText(this, "Activity onRestart", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(Globals.TAG, "Activity 1 onDestroy")
        Toast.makeText(this, "Activity onDestroy", Toast.LENGTH_SHORT).show()
    }
}