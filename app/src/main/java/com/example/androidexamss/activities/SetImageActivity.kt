package com.example.androidexamss.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.androidexamss.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

class SetImageActivity : AppCompatActivity() {

    lateinit var xBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_image)

       setImage()
    }

    private fun setImage() {
        xBtn = findViewById(R.id.x_btn)

        var image_link = intent.getStringExtra("ImgLink")
        var imgPlaceholder = findViewById<ImageView>(R.id.img_placeholder)

        CoroutineScope(IO).launch {
            var inputStream: InputStream = URL(image_link).content as InputStream
            var d: Drawable = Drawable.createFromStream(inputStream, "src name")

            withContext(Dispatchers.Main) {
                imgPlaceholder.setImageDrawable(d)
            }
        }

        xBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)

            startActivity(intent)
        }
    }
}