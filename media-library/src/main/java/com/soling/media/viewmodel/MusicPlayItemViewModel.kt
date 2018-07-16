package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.repository.PlayModes
import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.model.MusicItem
import com.soling.media.player.PlayStates

/**
 * 音乐播放单条曲目的ViewModel(还没有重命名为MusicPlayItemModel)
 */
class NewPlayItemViewModel(app: Application, playItemRepository: PlayItemRepository) : BasePlayItemViewModel(app,playItemRepository) {

    val playItem = MediatorLiveData<MusicItem>()

     init {
         playItem.addSource(mediaItem, {
             playItem.value = it as MusicItem?  //父类中的MediaItem转成具体的MusicItem
         })
     }

 }