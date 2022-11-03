package com.example.androidexamss.dataAdapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidexamss.DbController
import com.example.androidexamss.Globals
import com.example.androidexamss.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SavedMainAdapter: RecyclerView.Adapter<SavedMainAdapter.ItemHolder>() {

    class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    }

    var initImageList = DbController.getAllOriginalImages()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_main_view, null)
        var imageLink =
            DbController.getAllResultFromOriginalImage(Globals.getInitImagePosition().toLong())
        val historyRc: RecyclerView = view.findViewById(R.id.childRc)
        var drawableList = CoroutineScope(IO).async { Globals.downloadImageFromGivenLink(imageLink) }

        CoroutineScope(Main).launch {
            val itemAdapter = SavedSecondAdapter(drawableList.await())
            historyRc.adapter = itemAdapter
        }

        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        holder.itemView.findViewById<ImageView>(R.id.svdInitImg)
            .setImageBitmap(BitmapFactory
                .decodeByteArray(initImageList[position]
                    .image, 0, initImageList[position]
                    .image.size))
    }

    override fun getItemCount(): Int {
        return initImageList.size
    }
}