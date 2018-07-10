package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.repository.PlayModes
import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.model.VideoItem
import com.soling.media.player.PlayStates
import com.soling.media.util.mylog

open class BasePlayItemViewModel(app: Application, private val playItemRepository: PlayItemRepository) : AndroidViewModel(app) {

    val mediaItem = MediatorLiveData<MediaItem>()


    val playModes = MediatorLiveData<PlayModes>()

    val playStates =  MediatorLiveData<PlayStates>()

    val playPosition = MediatorLiveData<Int>()

     init {
           mediaItem.addSource(playItemRepository.currentItemLiveData, {
               mediaItem.value = it
               it?.let {
                   playItemRepository.player.setDataSource(it.realPath)
                   play_pause(true)
                   seekTo(it.currentPosition.toInt())
               }
           })

         playModes.addSource(playItemRepository.playModes,{
             playModes.value = it
         })

         playStates.addSource(playItemRepository.playStates,{
             playStates.value = it
         })

         playPosition.addSource(playItemRepository.position,{
             playPosition.value = it
         })

     }

    fun replaceList(list: List<MediaItem>) {
        playItemRepository.replacePlaylist(list)
    }

    fun playItem(playItem:MediaItem) {
        playItemRepository.playItem(playItem)
    }



    fun next() {
        playItemRepository.next()
    }

    fun previous() {
        playItemRepository.previous()
    }

    fun seekTo(position: Int) {
        playItemRepository.seekTo(position)
    }

    fun switchPlayModes() {
        playItemRepository.switchPlayModes()
    }

    fun play_pause(checked:Boolean) {
        if(checked)playItemRepository.play()else playItemRepository.pause()
    }

    fun unmount() {
        playItemRepository.unmount()
    }

    fun stop() {
        playItemRepository.stop()
    }

}