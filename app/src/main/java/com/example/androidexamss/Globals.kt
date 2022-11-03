package com.example.androidexamss

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.example.androidexamss.controller.Web
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.*
import java.net.URL

object Globals {

    val TAG = "AndroidLifeCycle"
    var isLoading: Boolean = false
    var isFullSized: Boolean = false
    var imageBitmap: Bitmap? = null
    var imagePos = -1


    var server: Web = Web()
    var fragmentManager: FragmentManager? = null


    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Photo(var thumbnail_link: String, var image_link: String, var identifier: String, var x: Int,
                     var y: Int,
                     var w: Int,
                     var h: Int,
                     var imageH: Int,
                     var imageW: Int,
                     var position: Int=-1) : Serializable {
                         
                     }
    data class originalImage(var id: Long, var image: ByteArray)

    fun getInitImagePosition(): Int {
        imagePos = imagePos + 1

        return imagePos
    }


    fun getBitmap(context: Context, id: Int?, uri: String?, decoder: (Context, Int?, String?) -> Bitmap): Bitmap {
        return decoder(context, id, uri)
    }

    fun UriToBitmap(context: Context, id: Int?, uri: String?): Bitmap {
        val image: Bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri.parse(uri))
        return image
    }

    fun requestAllEndpointImages(image_link: String?, context: Context?) {

        CoroutineScope(IO).launch {

            try {
                coroutineScope {
                    listOf(
                        async { Log.i(TAG, "Google request")
                            server.GETImage("http://api-edu.gtl.ai/api/v1/imagesearch/google", image_link
                                , context)
                        },
                        async { Log.i(TAG, "Bing request")
                            server.GETImage("http://api-edu.gtl.ai/api/v1/imagesearch/bing", image_link
                                , context)
                        },
                        async { Log.i(TAG, "Tineye request")
                            server.GETImage("http://api-edu.gtl.ai/api/v1/imagesearch/tineye", image_link
                                , context)
                        },
                    ).awaitAll()
                }
            }catch (e: Throwable) {
                Log.i(TAG, e.toString())
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun upload(url: String, imageBitmap: Bitmap?, context: Context?, view: View) {
        var img = getFormatFromBit(imageBitmap, view, "picture.png")
        server.POSTImage(url, img, imageBitmap, context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormatFromBit(bitmap: Bitmap?, view: View, filename: String): File {
        //transfering to a file and uploading to server
        val byteArrayOutputStream = ByteArrayOutputStream()
        val img = File(view.context.cacheDir, filename)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 20, byteArrayOutputStream)

        val fileOutpurStream = FileOutputStream(img)
        fileOutpurStream.write(byteArrayOutputStream.toByteArray())
        fileOutpurStream.close()
        return img
    }

    suspend fun downloadImageFromGivenLink(imageList: List<Globals.Photo>): List<Drawable> {
        var drawableList = mutableListOf<Drawable>()
        var imList = CoroutineScope(IO).async {

            for(i in imageList) {
                var inputStream: InputStream = URL(i.thumbnail_link).content as InputStream
                var drawable: Drawable = Drawable.createFromStream(inputStream, "src name")

                drawableList.add(drawable)
            }

            return@async drawableList
        }

        return imList.await()
    }

}