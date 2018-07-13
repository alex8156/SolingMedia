package com.soling.media.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.soling.media.persistence.model.VideoItem

/**
 * Created by yizeng on 2018/5/8.
 */
@Dao
abstract class VideoDao : BaseDao<VideoItem> {

    @Query("SELECT * FROM VideoItem where usbRootPath = :usbRootPath and isExists = 1")
    abstract override fun loadList(usbRootPath: String?): LiveData<List<VideoItem>>


    @Query("update VideoItem set usbRootPath =''")
    abstract override fun deleteAll()

    @Query("SELECT * FROM VideoItem where path = :path limit 1")
    abstract  fun loadMedia(path :String):VideoItem?

    @Query("SELECT * FROM VideoItem where isExists = 1 limit 1")
    abstract override fun findFirst(): VideoItem?

    @Query("SELECT * FROM VideoItem where isPlayedRecent = 1 and isExists = 1 limit 1")
    abstract override fun findPlayedRecent() : VideoItem?

    @Query("UPDATE  VideoItem  SET isExists = 0  where usbRootPath = :usbRootPath")
    abstract override fun setAllNotExist(usbRootPath :String?)

}