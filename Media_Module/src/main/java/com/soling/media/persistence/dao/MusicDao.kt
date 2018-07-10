package com.soling.media.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.soling.media.persistence.model.MusicItem

/**
 * Created by yizeng on 2018/5/8.
 */
@Dao
abstract class  MusicDao : BaseDao<MusicItem> {


//    @Query("SELECT * FROM MusicItem")
//    abstract fun loadMediaList(): Flowable<List<MusicItem>>

    @Query("SELECT * FROM MusicItem where usbRootPath = :usbRootPath and isExists = 1")
    abstract override fun loadList(usbRootPath :String?):LiveData<List<MusicItem>>

    @Query("SELECT * FROM MusicItem where path = :path limit 1")
    abstract  fun loadMedia(path :String):MusicItem?

    @Query("update MusicItem set usbRootPath ='' ")
    abstract override fun deleteAll()

    @Query("DELETE  FROM MusicItem where displayName = :displayName")
    abstract override fun deleteByDisplayName(displayName : String)

    @Query("SELECT * FROM MusicItem where isExists =1  limit 1")
    abstract override fun findFirst(): MusicItem?

    @Query("SELECT * FROM MusicItem where isPlayedRecent = 1 limit 1")
    abstract override fun findPlayedRecent() : MusicItem?

    @Query("UPDATE  VideoItem  SET isExists = 0  where usbRootPath = :usbRootPath")
    abstract override fun setAllNotExist(usbRootPath :String?)

    @Query("SELECT * FROM MusicItem  where isFavorite = 1")
    abstract  fun loadFavorite():LiveData<List<MusicItem>>

    fun loadFavoriteDistinct():LiveData<List<MusicItem>> = loadFavorite().getDistinct()

//    override fun loadDistinctList(usbRootPath: String?): LiveData<List<MusicItem>> = loadList(usbRootPath).getDistinctE()

     fun LiveData<List<MusicItem>>.getDistinctE(): LiveData<List<MusicItem>> {
         val distinctLiveData = MediatorLiveData<List<MusicItem>>()
         distinctLiveData.addSource(this, object : Observer<List<MusicItem>> {
             private var initialized = false
             private var lastObj: List<MusicItem>? = null

             override fun onChanged(obj: List<MusicItem>?) {
                 if (!initialized) {
                     initialized = true
                     lastObj = obj
                     distinctLiveData.postValue(lastObj)
                 } else if ((obj == null && lastObj != null)
                         || obj != lastObj) {
                     lastObj = obj
                     distinctLiveData.postValue(lastObj)
                 }
             }
         })

         return distinctLiveData
    }

}