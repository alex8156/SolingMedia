package com.soling.media.persistence.repository

import android.arch.persistence.room.Database
import android.arch.persistence.room.InvalidationTracker
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.soling.media.persistence.dao.*
import com.soling.media.persistence.model.*

/**
 * Created by yizeng on 2018/5/8.
 */
@Database(entities = arrayOf(MediaItem::class, MusicItem::class, VideoItem::class, PhotoItem::class),version = 1)
abstract class MediaDatabase : RoomDatabase() {

    abstract fun musicDao() : MusicDao

    abstract fun videoDao() : VideoDao

    abstract fun photoDao() : PhotoDao


    companion object {

        @Volatile private var INSTANCE: MediaDatabase? = null

        fun getInstance(context: Context): MediaDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        MediaDatabase::class.java, "usb.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }


}