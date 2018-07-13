package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.launch

class PlayItemViewModelFactory private constructor(private val application: Application,val playItemRepository: PlayItemRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (AndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            if(NewPlayItemViewModel::class.java.isAssignableFrom(modelClass)) {
                return NewPlayItemViewModel(application,playItemRepository) as T
            } else if(VideoPlayItemViewModel::class.java.isAssignableFrom(modelClass)) {
                launch {
//                    playItemRepository.player.getIMediaPlayer().getSurfaceMutex()?.lock()
                    mylog("锁住surface锁")
                }
                return VideoPlayItemViewModel(application,playItemRepository) as T
            }

        }
        throw IllegalArgumentException("You must extend AndroidViewModel and  MediaListRepository class")
    }

    companion object {

        @Volatile
        private var INSTANCE: PlayItemViewModelFactory? = null

        fun getInstance(application: Application,playItemRepository: PlayItemRepository): PlayItemViewModelFactory =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: PlayItemViewModelFactory(application,playItemRepository).also { INSTANCE = it }
                }
    }
}


