package com.soling.media.player

/**
 * Created by yizeng on 2018/5/11.
 */
interface Player /*: ReadWriteProperty<Any?, MediaItem>*/{

    fun play()

    fun pause()

    fun setDataSource(path: String)

    fun getIMediaPlayer() : IMediaPlayer

    fun seekTo(position: Int)
    fun stop()

}
enum class PlayStates {STOPPED, PLAYING,PAUSED}

