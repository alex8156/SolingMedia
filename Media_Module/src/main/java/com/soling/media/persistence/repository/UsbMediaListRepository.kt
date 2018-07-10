package com.soling.media.persistence.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import com.soling.media.persistence.dao.BaseDao
import com.soling.media.persistence.model.MediaItem
import com.soling.media.scan.Scanner
import com.soling.media.util.ViewFlags
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

open  class UsbMediaListRepository  constructor(val context: Context, val scanner: Scanner<*>, val dao :BaseDao<Any>)  {

    val usbListLiveData = MediatorLiveData<List<Any?>>()

    var localLiveData: LiveData<out List<Any?>>?= null

    var autoPlayLiveData = MediatorLiveData<Any>()
//    val path = MutableLiveData<String>()

    val viewFlagLiveData = MediatorLiveData<Int>().apply { value = 0 }

    var  usbRootPath :String ? = null

    init {
        usbListLiveData.addSource(scanner.scanLiveData, Observer {
//            usbListLiveData.value = it
//            launch {
//                it?.forEach {
//                    metadataParser.parseMetadata(it)
//                    mylog("$it","parseMetadata");
//                    dao.insert(it)
//                }
////                it?.let { if(it.isNotEmpty())dao.updateAll(*it.toTypedArray() as Array<MusicItem>)}
//            }
            val firstItemJob = launch {
                autoPlayLiveData.postValue(dao.findPlayedRecent()?:dao.findFirst())
            }
            mylog("scan finished==>")
            localLiveData = dao.loadDistinctList(this.usbRootPath)
            usbListLiveData.addSource(localLiveData!!,Observer {
                mylog("watch mediaDatabase changed==>${it?.size}")
                val resetFlag = viewFlagLiveData.value!! and (( ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG)).inv()
                viewFlagLiveData.value = resetFlag or if(it.orEmpty().isNotEmpty()) ViewFlags.LIST_NOT_EMPTY_FLAG else 0
                launch { firstItemJob.join()
                    usbListLiveData.postValue(it) }
            })


        })

    }

     fun loadMusicList(pathName: String) {
        scanner.scan(pathName)

    }

    fun  updateItem (obj : Any) = launch {dao.update(obj)  }

    fun deleteItem(obj: Any) = launch { dao.delete(obj) }

    fun deleteByDisplayName(displayName : String) = launch { dao.deleteByDisplayName(displayName) }

    fun mount(usbRootPath: String) {
        usbRootPath?.let {
            this.usbRootPath = usbRootPath
            val resetAllFlag  = viewFlagLiveData.value!! and(ViewFlags.USB_IN_FLAG or ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG or ViewFlags.PARK_ENABLE_FLAG).inv()
            viewFlagLiveData.value = (resetAllFlag or ViewFlags.USB_IN_FLAG or ViewFlags.SCAN_START_FLAG)
            mylog("mount==>viewFlag=${viewFlagLiveData.value}")
            loadMusicList(usbRootPath) }
    }

    fun unmount() {
        val resetAllFlag  = viewFlagLiveData.value!! and(ViewFlags.USB_IN_FLAG or ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG or ViewFlags.PARK_ENABLE_FLAG).inv()
        viewFlagLiveData.value = resetAllFlag
        mylog("unmount==>viewFlag=${viewFlagLiveData.value}")
       if(localLiveData!= null) usbListLiveData.removeSource(localLiveData!!)
        scanner.cancelScan()
        launch { dao.setAllNotExist(usbRootPath) }
        autoPlayLiveData.value = null
//        async { dao.deleteAll() }
//        scanner.scanLiveData.value = listOf()
    }

    fun parkingPlay(enable: Boolean) {
        val resetParkFlag = viewFlagLiveData.value!! and ViewFlags.PARK_ENABLE_FLAG.inv()
        viewFlagLiveData.postValue( resetParkFlag or if(enable) ViewFlags.PARK_ENABLE_FLAG else 0)
        mylog("parkingPlay线程=>${Thread.currentThread().name};parkingPlay=${enable};viewFlag=${viewFlagLiveData.value}")
    }


    companion object {
        @Volatile
        private var INSTANCE: UsbMediaListRepository? = null

        fun getInstance(context: Context, scanner: Scanner<*>, dao :BaseDao<Any>): UsbMediaListRepository = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: UsbMediaListRepository(context, scanner, dao).also {
                        INSTANCE = it }
                }


    }


}