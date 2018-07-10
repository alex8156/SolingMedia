package com.soling.music.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.viewmodel.InternalMusicListViewModel
import com.soling.media.viewmodel.UsbMediaViewModel

class InternalMediaViewModelFactory private constructor(val application: Application, val mediaListRepository: UsbMediaListRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (AndroidViewModel::class.java.isAssignableFrom(modelClass) && modelClass.isAssignableFrom(InternalMusicListViewModel::class.java)) {
            val internalMediaViewModel = InternalMusicListViewModel(application,mediaListRepository)
            return (internalMediaViewModel) as T
        }
        throw IllegalArgumentException("You must extend AndroidViewModel and  MediaListRepository class")
    }


    companion object {

        @Volatile
        private var INSTANCE: InternalMediaViewModelFactory? = null

        fun getInstance(application: Application, mediaListRepository: UsbMediaListRepository): InternalMediaViewModelFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: InternalMediaViewModelFactory(application,mediaListRepository).also { INSTANCE = it }
                }
    }
}