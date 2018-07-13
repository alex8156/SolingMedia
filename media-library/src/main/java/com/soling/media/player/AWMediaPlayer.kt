package com.soling.media.player

import android.media.AudioManager
import android.media.MediaPlayer
import android.view.SurfaceHolder
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock

class AWMediaPlayer() : IMediaPlayer {
    override fun stop() {
        awMediaPlayer.stop()
    }

    val prepared = Mutex()

    val setSurface = Mutex()

    override fun getSurfaceMutex(): Mutex? = setSurface

    init {
        mylog("AWMediaPlayer初始化")
    }

    override fun getCurrentPosition(): Int {
        return awMediaPlayer.currentPosition
    }


    override fun setOnCompletion(onCompletion: () -> Unit) {
        awMediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

     override fun setOnVideoSizeChangedListener(videoSizeChanged:(Int, Int)->Unit) {
        awMediaPlayer.setOnVideoSizeChangedListener{ mediaPlayer: MediaPlayer, width: Int, height: Int ->
            videoSizeChanged(width,height)
        }
    }

    override  fun play() {
        launch {
//            mylog("播放锁之前")
            prepared.withLock {
                mylog("准备播放")
                awMediaPlayer.start()
            }
//            mylog("播放锁之后")
        }

    }

    override fun seekTo(position: Int) {
        launch {
//            mylog("定位锁之前")
            prepared.withLock {
                mylog("准备定位:$position")
                awMediaPlayer.seekTo(position)
            }
//            mylog("定位锁之后")
        }

    }

    override fun pause() {
        launch {
            prepared.withLock {
                awMediaPlayer.pause()
            }
        }

    }

    override fun setDataSource(path : String ) {
        launch {
//            mylog("设置播放路径之前")
            prepared.lock()
            awMediaPlayer.reset()
            setSurface.withLock {
                awMediaPlayer.setDataSource(path)
                awMediaPlayer.prepare()
            }
//            mylog("设置播放路径之后")080935711294
        }

    }

    override fun setSurface(surface: SurfaceHolder?){
        awMediaPlayer.setDisplay(surface)
//        setSurface.unlock()
        mylog("Surface设置好了")
    }


    val awMediaPlayer : MediaPlayer = MediaPlayer().apply {
        setAudioStreamType(AudioManager.STREAM_MUSIC)
        setOnPreparedListener({
            mylog("准备好了")
//            play()
            prepared.unlock()
//            mylog("准备好了释放锁")
        })
        setOnErrorListener({
            mp, what, extra ->
            mylog("onError==>what=$what;extra=>$extra")
            what == -38 || what == 100
        })
        setOnInfoListener({mp, what, extra ->  true})


    }

}