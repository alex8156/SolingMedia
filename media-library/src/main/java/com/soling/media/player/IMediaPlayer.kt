package com.soling.media.player

import android.view.Surface
import android.view.SurfaceHolder
import kotlinx.coroutines.experimental.sync.Mutex

interface IMediaPlayer {

    fun play()

    fun pause()

    fun setDataSource(path : String)

    fun setOnCompletion(onCompletion :()->Unit)


    fun getCurrentPosition() : Int

//      get() = MutableLiveData<PlayStates>()

    fun seekTo(position:Int)

    fun setSurface(surface: SurfaceHolder?) = Unit

    fun getSurfaceMutex():Mutex?=null

    fun setSurface(surface: Surface) = Unit

    fun setOnVideoSizeChangedListener(videoSizeChanged:(Int,Int)->Unit) = Unit

    fun stop()

}