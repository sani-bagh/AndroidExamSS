package com.example.androidexamss.dataAdapters

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidexamss.DbController
import com.example.androidexamss.Globals
import com.example.androidexamss.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import com.example.androidexamss.R.drawable
import com.example.androidexamss.activities.SetImageActivity

class ImageItemAdapter: RecyclerView.Adapter<ImageItemAdapter.ItemHolder>() {

    class ItemHolder(private val view: View): RecyclerView.ViewHolder(view) {

    }

    private val bingResult: List<Globals.Photo> =
        DbController.getAllBingResults(DbController.getIdOfLastImage())
    private val tineyeResult: List<Globals.Photo> =
        DbController.getAllTineyeResults(DbController.getIdOfLastImage())
    private val googleResult: List<Globals.Photo> =
        DbController.getAllGoogleResults(DbController.getIdOfLastImage())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, null)

        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, index: Int) {
        var listViewOne: ImageView = holder.itemView.findViewById(R.id.list_viewOne)
        var listViewTwo: ImageView = holder.itemView.findViewById(R.id.list_viewTwo)
        var listViewThree: ImageView = holder.itemView.findViewById(R.id.list_viewThree)

        listViewOne.setOnClickListener {

            if (bingResult.isNotEmpty()) {
                val intent = Intent(holder.itemView.context, SetImageActivity::class.java)
                intent.putExtra("ImgLink", bingResult[index].image_link)
                Globals.isFullSized = true
                holder.itemView.context.startActivity(intent)
            }


        }

        listViewTwo.setOnClickListener {

            if (googleResult.isNotEmpty()) {

                val intent = Intent(holder.itemView.context, SetImageActivity::class.java)
                intent.putExtra("ImgLink", googleResult[index].image_link)
                Globals.isFullSized = true
                holder.itemView.context.startActivity(intent)
            }
        }

        listViewThree.setOnClickListener {
            if (tineyeResult.isNotEmpty()) {

                val intent = Intent(holder.itemView.context, SetImageActivity::class.java)
                intent.putExtra("ImgLink", tineyeResult[index].image_link)
                Globals.isFullSized = true
                holder.itemView.context.startActivity(intent)
            }
        }

        CoroutineScope(IO).launch {

            val drawableBing: List<Drawable> = Globals.downloadImageFromGivenLink(bingResult)
            val drawableTineye: List<Drawable> = Globals.downloadImageFromGivenLink(tineyeResult)
            val drawableGoogle: List<Drawable> = Globals.downloadImageFromGivenLink(googleResult)

            withContext(Dispatchers.Main) {

                if (drawableBing.isEmpty()) {

                    listViewOne.setImageResource(drawable.notfound)
                } else {
                    listViewOne.load(drawableBing[index])
                }
                if (drawableGoogle.isEmpty()) {
                    listViewTwo.setImageResource(drawable.notfound)
                } else {
                    listViewTwo.load(drawableGoogle[index])
                }
                if (drawableTineye.isEmpty()) {
                    listViewThree.setImageResource(drawable.notfound)
                } else {
                    listViewThree.load(drawableTineye[index])
                }
            }

        }

    }



    override fun getItemCount(): Int {
        return bingResult.size
    }


}