package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import com.soling.media.util.mylog
import com.soling.media.persistence.repository.UsbMediaListRepository

class InternalMusicListViewModel(app: Application,val  mediaListRepository: UsbMediaListRepository) : AndroidViewModel(app) {

    val intenalLiveData = MediatorLiveData<List<Any?>>()

    init {
        mylog("InternalMusicListViewModel==>init")
        intenalLiveData.addSource(mediaListRepository.usbListLiveData,{
            mylog("InternalMusic finish==>${it?.size}")
            it?.let{
                intenalLiveData.value = it
            }
        })
    }


    fun updateItem(obj : Any) =  mediaListRepository.updateItem(obj)

    fun deleteItem(obj: Any) = mediaListRepository.deleteItem(obj)


}