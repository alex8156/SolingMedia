package com.soling.media.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.soling.media.persistence.model.PhotoItem

/**
 * Created by yizeng on 2018/5/8.
 */
@Dao
abstract class PhotoDao : BaseDao<PhotoItem> {

    @Query("SELECT * FROM PhotoItem where usbRootPath = :usbRootPath")
    abstract override fun loadList(usbRootPath :String?): LiveData<List<PhotoItem>>



    @Query("update PhotoItem set usbRootPath =''")
    abstract override fun deleteAll()


}