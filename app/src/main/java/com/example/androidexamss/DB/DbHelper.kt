package com.example.androidexamss

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONArray
import java.time.LocalDateTime

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys=ON;")
        db.execSQL("create table init_images (init_img_id integer primary key, date_searched text, image blob)")
        db.execSQL("create table images (id INTEGER PRIMARY KEY, date_saved TEXT, image BLOB, thumbnail BLOB, saved_from TEXT, init_img_id INTEGER, FOREIGN KEY(init_img_id) REFERENCES init_images (init_img_id));")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("drop table if exists init_images")
        db.execSQL("drop table if exists images")
        onCreate(db)
    }


    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ImagesDb.db"
    }
}

object DbController {
    private lateinit var feedReaderDbHelper: FeedReaderDbHelper

    fun initDb(context: Context) {
        feedReaderDbHelper = FeedReaderDbHelper(context)
    }

    fun getAllBingResults(selectedImage: Long) : List<Globals.Photo> {
        val query = "select * from images where init_img_id='$selectedImage' and saved_from='bing';"

        val c: Cursor = feedReaderDbHelper.writableDatabase.rawQuery(query, null)
        val resultList = mutableListOf<Globals.Photo>()

        while (c.moveToNext()) {
            val photo = Globals.Photo(c.getString(c.getColumnIndexOrThrow("thumbnail")),
                c.getString(c.getColumnIndexOrThrow("image")),
                c.getString(c.getColumnIndexOrThrow("saved_from")),
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1)
            resultList.add(photo)
        }

        return resultList
    }

    fun getAllTineyeResults(slectedImage: Long): List<Globals.Photo> {
        val query = "SELECT * FROM images WHERE init_img_id='$slectedImage' AND saved_from ='tineye';"

        val c : Cursor = feedReaderDbHelper.writableDatabase.rawQuery(query, null)
        val resultList = mutableListOf<Globals.Photo>()
        while (c.moveToNext()) {

            val searchResult = Globals.Photo(c.getString(c.getColumnIndexOrThrow("thumbnail")),
                c.getString(c.getColumnIndexOrThrow("image")),
                c.getString(c.getColumnIndexOrThrow("saved_from")),
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1)
            resultList.add(searchResult)

        }
        return resultList
    }

    fun getAllGoogleResults(selectedImage: Long) : List<Globals.Photo> {
        val query = "SELECT * FROM images WHERE init_img_id='$selectedImage' AND saved_from ='google';"

        val c : Cursor = feedReaderDbHelper.writableDatabase.rawQuery(query, null)
        val resultList = mutableListOf<Globals.Photo>()
        while (c.moveToNext()) {

            val searchResult = Globals.Photo(c.getString(c.getColumnIndexOrThrow("thumbnail")),
                c.getString(c.getColumnIndexOrThrow("image")),
                c.getString(c.getColumnIndexOrThrow("saved_from")),
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1)
            resultList.add(searchResult)

        }
        return resultList
    }

    fun getAllOriginalImages(): MutableList<Globals.originalImage> {
        val query = "select * from init_images;"

        val cursor: Cursor = feedReaderDbHelper.writableDatabase.rawQuery(query, null)
        var originalImageList = mutableListOf<Globals.originalImage>()

        while (cursor.moveToNext()) {
            var image = Globals.originalImage(cursor.getLong(cursor.getColumnIndexOrThrow("init_img_id")),
                cursor.getBlob(cursor.getColumnIndexOrThrow("image")))

            originalImageList.add(image)
        }

        return originalImageList
    }

    fun getAllResultFromOriginalImage(id: Long): MutableList<Globals.Photo> {
        var query = "select * from images where init_img_id='$id'"
        val cursor: Cursor = feedReaderDbHelper.writableDatabase.rawQuery(query, null)
        var specificSearchList = mutableListOf<Globals.Photo>()

        while (cursor.moveToNext()) {
            var image = Globals.Photo(cursor.getString(cursor.getColumnIndexOrThrow("thumbnail")),
                cursor.getString(cursor.getColumnIndexOrThrow("image")),
                cursor.getString(cursor.getColumnIndexOrThrow("saved_from")),
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1)
            specificSearchList.add(image)
        }

        return specificSearchList
    }

    fun insertInitlImage(dateSearched: String, image: ByteArray) {
        feedReaderDbHelper.writableDatabase.insert("init_images", null, ContentValues().apply {
            put("date_searched", dateSearched)
            put("image", image)
        })
    }

    fun getIdOfLastImage(): Long {
        val c: Cursor = feedReaderDbHelper.writableDatabase.rawQuery("select * from init_images order by init_img_id desc limit 1", null)
        var initImgID: Long = 0
        while (c.moveToNext()) {
            initImgID = c.getLong(c.getColumnIndexOrThrow("init_img_id"))
        }

        return initImgID
    }

    fun insertSearchedImages(dateSaved: String, image: String, thumbnail: String, identifier: String, imgFK: Long) {
        feedReaderDbHelper.writableDatabase.insert("images", null, ContentValues().apply {
            put("date_saved", dateSaved)
            put("image", image)
            put("thumbnail", thumbnail)
            put("saved_from", identifier)
            put("init_img_id", imgFK)
        })
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun saveListInDb(bingResponse: JSONArray?) {
        val mapper = jacksonObjectMapper()

        val bingList: List<Globals.Photo> = mapper.readValue(bingResponse.toString())

        var fk = getIdOfLastImage()

        for (i in bingList) {
            insertSearchedImages(LocalDateTime.now().toString(), i.image_link, i.thumbnail_link, i.identifier, fk)
        }
    }
}