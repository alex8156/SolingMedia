package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.soling.media.persistence.repository.UsbMediaListRepository

class UsbMediaViewModelFactory private constructor(val application: Application,val mediaListRepository: UsbMediaListRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (AndroidViewModel::class.java.isAssignableFrom(modelClass) && modelClass.isAssignableFrom(UsbMediaViewModel::class.java)) {
            val usbMediaViewMode = UsbMediaViewModel(application,mediaListRepository)
            UsbMediaViewModelPool.getInstance().add(usbMediaViewMode)
            return (usbMediaViewMode) as T
        }
        throw IllegalArgumentException("You must extend AndroidViewModel and  MediaListRepository class")
    }


    companion object {

        @Volatile
        private var INSTANCE: UsbMediaViewModelFactory? = null

        fun getInstance(application: Application,mediaListRepository: UsbMediaListRepository): UsbMediaViewModelFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: UsbMediaViewModelFactory(application,mediaListRepository).also { INSTANCE = it }
                }
    }
}





