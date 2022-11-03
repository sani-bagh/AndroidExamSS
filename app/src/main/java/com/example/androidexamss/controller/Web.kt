package com.example.androidexamss.controller

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.example.androidexamss.DbController
import com.example.androidexamss.Globals
import com.example.androidexamss.R
import com.example.androidexamss.fragments.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime

class Web {

    var imageUrl = "Loading"



    @Throws(InterruptedException::class)
    fun POSTImage(url: String?, image: File?, imageBitmap: Bitmap?, context: Context?) {
        AndroidNetworking.upload(url)
            .addMultipartFile("image", image)
            .addMultipartParameter("key", "value")
            .setTag("Upload")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener{ _, _ -> }
            .getAsString(object : StringRequestListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(response: String) {
                    imageUrl = response

                    Log.i(Globals.TAG, imageUrl)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    imageBitmap?.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream)
                    val byteArrImg = byteArrayOutputStream.toByteArray()

                    DbController.insertInitlImage(LocalDateTime.now().toString(), byteArrImg)

                    CoroutineScope(IO).launch {
                        Globals.requestAllEndpointImages(imageUrl, context)
                    }

                }

                override fun onError(error: ANError) {
                    println(error.response)
                }
            })
    }

    @Throws(InterruptedException::class)
    fun GETImage(url: String?, image_link: String?, context: Context?) {
        AndroidNetworking.get(url)
            .addQueryParameter("url", image_link)
            .setPriority(Priority.HIGH)
            .addHeaders("Content-Type", "multipart/form-data")
            .setTag("Get images")
            .build()
            .setUploadProgressListener { _, _ ->
                Log.i(
                    Globals.TAG,
                    "Loading..."
                )
            }
            .getAsJSONArray(object : JSONArrayRequestListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(response: JSONArray) {
                    var imageProp: JSONArray = response

                    if (imageProp.length() > 0) {
                        DbController.saveListInDb(imageProp)
                        Log.i(Globals.TAG, "Data: $imageProp")
                        Globals.isLoading = false

                        Toast.makeText(context, "Reverse search is ready", Toast.LENGTH_SHORT).show()

                        Globals.imageBitmap = null
                        Globals.fragmentManager?.beginTransaction()?.replace(R.id.placeholder, SearchFragment())?.commit()


                    } else {
                        Log.i(Globals.TAG, "No data found")
                    }
                }

                override fun onError(error: ANError) {
                    println(error.response)
                }
            })
    }
}