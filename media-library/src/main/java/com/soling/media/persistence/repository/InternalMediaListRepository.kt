package com.soling.media.persistence.repository

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.soling.media.util.moveTo
import com.soling.media.Injection
import com.soling.media.persistence.dao.BaseDao
import com.soling.media.persistence.model.MusicItem
import com.soling.media.scan.Scanner
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

/**
 * 本地数据列表数据来源
 */
class InternalMediaListRepository(context: Context,scanner: Scanner<*>,dao : BaseDao<Any>):UsbMediaListRepository(context,scanner,dao) {


    companion object {
        @Volatile
        private var INSTANCE: InternalMediaListRepository? = null

        fun getInstance(context: Context, scanner: Scanner<*>, dao :BaseDao<Any>): InternalMediaListRepository = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: InternalMediaListRepository(context, scanner, dao).also {
                                INSTANCE = it }
                }

    }




//    val intenalLiveData = MutableLiveData<List<MusicItem>>()
//
//    fun loadMusicList() {
//
//        val job = async {
//            val cursor = mContext.contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, Query.projection, Query.selection,null,null)
//            val mutableList = mutableListOf<MusicItem>()
//            cursor.moveTo(mutableList) {
//                val path =  cursor.getStringOrNull(MediaStore.Audio.Media.DATA)
//                val displayName =  cursor.getStringOrNull(MediaStore.Audio.Media.DISPLAY_NAME)
//                val artist =  cursor.getStringOrNull(MediaStore.Audio.Media.ARTIST)
//                val album =  cursor.getStringOrNull(MediaStore.Audio.Media.ALBUM)
//                val duration = cursor.getStringOrNull(MediaStore.Audio.Media.DURATION)
//                MusicItem().apply {
//                    this.path = path ?: ""
//                    this.displayName = displayName ?:""
//                    this.artist = artist;
//                    this.album = album
//                    this.duration = duration?.toLong()
//                    Injection.provideFavoriteRepository(mContext as Application).isFavorite(this)
//                    mylog("internal music==>${it}")
//                }
//            }
//            mutableList.toList()
//        }
//
//        runBlocking { intenalLiveData.postValue(job.await()) }
//
//    }
//
//    fun update(item : MusicItem) {
//        launch {
//            intenalLiveData.value?.apply {
//                val newList = mutableListOf<MusicItem>()
//                newList.addAll(this);
//                newList[newList.indexOf(item)] = item
//                intenalLiveData.postValue(newList)
//            }
//        }
//    }
//
//
//    companion object {
//        @Volatile
//        private var INSTANCE: InternalMediaListRepository? = null
//
//        fun getInstance(mContext: Context): InternalMediaListRepository = INSTANCE
//                ?: synchronized(this) {
//                    INSTANCE
//                            ?: InternalMediaListRepository(mContext).also {
//                        INSTANCE = it }
//                }
//
//
//    }
//
//
//
////    fun queryInternalMusic(context: Context,mutableList: MutableList<MusicItem>) {
////        loadFavoriteMusicList()
////        val cursor = context.contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,Query.projection,Query.selection,null,null)
////        cursor.move(mutableList) {
////            val path =  cursor.getStringOrNull(MediaStore.Audio.Media.DATA)
////            val displayName =  cursor.getStringOrNull(MediaStore.Audio.Media.DISPLAY_NAME)
////            val artist =  cursor.getStringOrNull(MediaStore.Audio.Media.ARTIST)
////            val album =  cursor.getStringOrNull(MediaStore.Audio.Media.ALBUM)
////            val duration = cursor.getStringOrNull(MediaStore.Audio.Media.DURATION)
////            MusicItem(path ?: "", displayName
////                    ?: "", artist, album, duration?.toLong()).apply {
////                isFavorite = this in favoriteMusicList.value as List
////            }
////        }
////    }
//
//
//
//
//
//
//
////    fun loadFavoriteMusicList() : MutableLiveData<List<FavoriteItem>> {
////        if(favoriteMusicList.value == null || favoriteMusicList.value!!.isEmpty()) {
////            return mediaDatabase.favoriteMusicDao().loadMediaList().let {
////                favoriteMusicList.postValue(it)
////                favoriteMusicList
////            }
////        } else {
////            return favoriteMusicList
////        }
////    }
//
//    object Query{
//        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
//
//        val projection = arrayOf(MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION)
//
//    }

}