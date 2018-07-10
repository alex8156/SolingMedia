package com.soling.media.viewmodel

import android.app.Application
import android.arch.lifecycle.MediatorLiveData
import android.view.SurfaceHolder
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.model.VideoItem
import com.soling.media.util.ScreenUtil
import com.soling.media.util.mylog

class VideoPlayItemViewModel(app: Application, val playItemRepository: PlayItemRepository) : BasePlayItemViewModel(app,playItemRepository) {

    val playItem = MediatorLiveData<VideoItem>()

    val videoRadio = MediatorLiveData<Float>()

    val screenWidth : Int by lazy {
        ScreenUtil.getScreenWidthPixels(app)
    }

    val screenHeight : Int by lazy {
        ScreenUtil.getScreenHeightPixels(app)
    }

    val screenRadio : Float by lazy {
        screenWidth / screenHeight.toFloat()
    }

     init {
         playItem.addSource(mediaItem, {
             playItem.value = it as VideoItem?
         })
         playItemRepository.player.getIMediaPlayer().setOnVideoSizeChangedListener{  width,height ->
             videoRadio.value = width  / height.toFloat()
             mylog("width=$width;height=$height,videoRadio=${videoRadio.value};screenRadio=$screenRadio")
         }
     }

    fun setSurfaceHolder(surface: SurfaceHolder?) {
        playItemRepository.player.getIMediaPlayer().setSurface(surface)
    }

 }