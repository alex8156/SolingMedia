package com.soling.media.persistence.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.*
import com.soling.media.persistence.model.MediaItem

/**
 * Created by yizeng on 2018/5/9.
 */
interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)

    /**
     * Insert an array of objects in the database.
     *
     * @param obj the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)

    /**
     * Update an object from the database.
     *
     * @param obj the object to be updated
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(vararg obj : T)

    /**
     * Delete an object from the database
     *
     * @param obj the object to be deleted
     */
    @Delete()
    fun delete(obj: T)

    @Query("DELETE  FROM MusicItem where displayName = :displayName")
    fun deleteByDisplayName(displayName : String)

    @Query("SELECT * FROM MusicItem where usbRootPath=:usbRootPath and isExists = 1")
    fun loadList(usbRootPath: String?): LiveData<List<T>>

    fun loadDistinctList(usbRootPath :String?) = loadList(usbRootPath).getDistinct()

    @Query("update MusicItem set usbRootPath ='' ")
    fun deleteAll()

    @Query("SELECT * FROM MusicItem where isExists = 1 limit 1")
    fun findFirst(): T?

    @Query("SELECT * FROM MusicItem where isPlayedRecent = 1 and isExists=1 limit 1")
    fun findPlayedRecent() : T?

    @Query("UPDATE  MusicItem  SET isExists = 0  where usbRootPath = :usbRootPath")
    fun setAllNotExist(usbRootPath :String?)

     fun <T> LiveData<T>.getDistinct(): LiveData<T> {
        val distinctLiveData = MediatorLiveData<T>()
        distinctLiveData.addSource(this, object : Observer<T> {
            private var initialized = false
            private var lastObj: T? = null

            override fun onChanged(obj: T?) {
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