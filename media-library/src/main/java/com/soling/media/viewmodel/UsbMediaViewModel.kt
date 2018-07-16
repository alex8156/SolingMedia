package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.soling.media.util.mylog
import com.soling.media.persistence.repository.UsbMediaListRepository

/**
 * 列表ViewModel
 */
class UsbMediaViewModel(app: Application,val mediaListRepository: UsbMediaListRepository)  :  AndroidViewModel(app) {

    val usbLiveData = MediatorLiveData<List<Any?>>()

    val viewFlagLiveData = MediatorLiveData<Int>()

    var autoPlayLiveData = MediatorLiveData<Any>()

    val mountFlagLiveData = MutableLiveData<Boolean>()

    init {
        mylog("UsbMediaViewModel==>init")

        viewFlagLiveData.addSource(mediaListRepository.viewFlagLiveData,{  viewFlagLiveData.value = it})

        usbLiveData.addSource(mediaListRepository.usbListLiveData, { list->
            mylog("scan Usb finished==>")
            mylog("currentItemobserver musiclist changed==>${list?.size}")
            list?.let {usbLiveData.value = list  }
            })

        autoPlayLiveData.addSource(mediaListRepository.autoPlayLiveData,{
            autoPlayLiveData.value = it
        })

    }

    fun updateItem(obj : Any) =  mediaListRepository.updateItem(obj)

    fun deleteItem(obj: Any) = mediaListRepository.deleteItem(obj)

    fun deleteItem(displayName : String) = mediaListRepository.deleteByDisplayName(displayName)

//    fun mount(usbRootPath : String) {
//        mediaListRepository.mount(usbRootPath)
//    }
//
//    fun unmount() {
//        mediaListRepository.unmount()
//    }


}