package com.soling.music.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.soling.media.Injection
import com.soling.media.persistence.dao.MusicDao
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.util.loadFavorite
import com.soling.media.util.mylog


class FavoriteListViewModel (app: Application) : AndroidViewModel(app){

    val favoriteMusicList = MediatorLiveData<List<MusicItem>>()

    val  mediaListRepository: UsbMediaListRepository = Injection.provideUsbMusicListRepository(app)

    init {
        mylog("FavoriteListViewModel==>init")
        favoriteMusicList.addSource(mediaListRepository.loadFavorite(),{
            mylog("FavoriteMusic finish==>${it?.size}")
            it?.let{
                favoriteMusicList.value = it
            }
        })
    }


}
