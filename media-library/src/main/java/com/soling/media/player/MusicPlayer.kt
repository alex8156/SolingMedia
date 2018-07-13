package com.soling.media.player

import com.soling.media.util.isFileExists
import com.soling.media.util.mylog


/**
 * Created by yizeng on 2018/5/11.
 */
class MusicPlayer private constructor(val mediaPlayer: IMediaPlayer) : Player {
    override fun stop() {
        mediaPlayer.stop()
    }

    override fun getIMediaPlayer(): IMediaPlayer {
        return mediaPlayer
    }

    override fun setDataSource(path : String) {
        mylog("setDataSouce===>"+path)
        if(path.isFileExists()) mediaPlayer.setDataSource(path)
    }

//    override fun getValue(thisRef: Any?, property: KProperty<*>): MediaItem {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//
//    }

    override fun play() {
        mediaPlayer.play()

    }

    override fun pause() {
        mediaPlayer.pause()

    }

    override fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
    }


    companion object {
        @Volatile
        private var INSTANCE: MusicPlayer? = null

        fun getInstance(mediaPlayer: IMediaPlayer): MusicPlayer = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: MusicPlayer(mediaPlayer).also {
                        INSTANCE = it }
                }

    }


}