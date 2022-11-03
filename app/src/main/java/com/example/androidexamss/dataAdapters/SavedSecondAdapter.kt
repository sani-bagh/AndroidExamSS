package com.example.androidexamss.dataAdapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidexamss.R

class SavedSecondAdapter(input: List<Drawable>) : RecyclerView.Adapter<SavedSecondAdapter.ItemHolder>() {

    class ItemHolder(private val view: View): RecyclerView.ViewHolder(view)

    var drawableList = input

    lateinit var imgPlaceholder: ImageView


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.save_second_view, null)

        imgPlaceholder = view.findViewById(R.id.secondImgV)

        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (drawableList.isEmpty()) {
            imgPlaceholder.setImageResource(R.drawable.notfound)
        } else {
            imgPlaceholder.load(drawableList[position])
        }
        holder.itemView.findViewById<TextView>(R.id.childText).text = "image$position"
    }

    override fun getItemCount(): Int {
        return drawableList.size
    }

}