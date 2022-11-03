package com.example.androidexamss.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.androidnetworking.AndroidNetworking
import com.example.androidexamss.DbController
import com.example.androidexamss.Globals
import com.example.androidexamss.Globals.UriToBitmap
import com.example.androidexamss.Globals.imageBitmap
import com.example.androidexamss.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FragmentUpload : Fragment() {

    lateinit var ivSelect: ImageView
    lateinit var btnSelect: Button
    lateinit var upload: Button

    var imageUri: Uri? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_upload, container, false)


        //network init
        AndroidNetworking.initialize(context)

        //init db
        DbController.initDb(requireContext())


        ivSelect = view.findViewById(R.id.iv_select)
        btnSelect = view.findViewById(R.id.btn_select)
        upload = view.findViewById(R.id.upload)

        if (imageBitmap != null) {
            ivSelect.setImageBitmap(imageBitmap)
        }

        btnSelect.setOnClickListener {
            getResult.launch(getDataFromGallery())
        }

        //send selected image
        upload.setOnClickListener {

            if (imageBitmap == null) {
                Toast.makeText(context, "Image is not valid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()

                CoroutineScope(IO).launch {
                    Log.i(Globals.TAG, "POST")

                    Globals.isLoading = true
                     async {
                         Globals.upload(
                             "http://api-edu.gtl.ai/api/v1/imagesearch/upload",
                             imageBitmap,
                             requireContext(),
                             view
                         )
                    }
                }
            }
        }



        return view
    }


    var getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {

            imageBitmap =
                Globals.getBitmap(requireContext(), null, it.data?.data.toString(), ::UriToBitmap)
            imageUri = it.data?.data
            ivSelect.setImageBitmap(imageBitmap)
        }
    }


    fun getDataFromGallery(): Intent {

        val i = Intent(Intent.ACTION_GET_CONTENT)

        i.type = "*/*"

        return i

    }


    companion object {


        @JvmStatic
        fun newInstance() = FragmentUpload()
    }
}