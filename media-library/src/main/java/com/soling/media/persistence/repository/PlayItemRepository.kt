package com.soling.media.persistence.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.soling.media.util.mylog
import com.soling.media.util.CycleNumber
import com.soling.media.persistence.dao.BaseDao
import com.soling.media.persistence.model.MediaItem
import com.soling.media.player.PlayStates
import com.soling.media.player.Player
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlin.properties.Delegates

class PlayItemRepository private constructor(val  player : Player, val dao: BaseDao<Any>) {

    var playList = listOf<MediaItem>()

    var currentItem : MediaItem by Delegates.observable(MediaItem()) {
        property, old, new  ->
            if(old.realPath.isNotEmpty()) {
                dao.update(old){
                    it.isPlayedRecent = false
                }
            }
            dao.update(new,{
                it.isPlayedRecent = true
            })
           currentItemLiveData.value = new
    }

     fun BaseDao<Any>.update(mediaItem: MediaItem,propertyChange:(MediaItem)->Unit) {
         launch {
             propertyChange(mediaItem)
             dao.update(mediaItem)
         }
    }



    val currentItemLiveData = MutableLiveData<MediaItem>()

    var cycleNumber : CycleNumber = CycleNumber(0);

    var playModeNumber : CycleNumber = CycleNumber(0, PlayModes.values().size)

    val position = MutableLiveData<Int>()

    val playStates =  MutableLiveData<PlayStates>()

    val currentItemobserver = Observer<MediaItem> {
        it?.let {
           player.setDataSource(it.realPath)
           play()
            seekTo(it.currentPosition.toInt())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PlayItemRepository? = null

        fun getInstance(player: Player, dao:BaseDao<Any>): PlayItemRepository = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: PlayItemRepository(player, dao).also {
                        INSTANCE = it }
                }

    }

    val playModes = MutableLiveData<PlayModes>().apply { value = PlayModes.SEQUENCE }

    val onCompletion = {
        mylog("onCompletion==>${playModes.value}")
        if(playList.isNotEmpty()) {
            pause()
            dao.update(currentItem) {
                it.currentPosition = 0
            }
            when(playModes.value ) {
                PlayModes.SEQUENCE,PlayModes.ALL -> next()
                PlayModes.REPEAT -> repeat()
                PlayModes.RANDOM -> random()
            }
        }
    }


    init {
        player.getIMediaPlayer().setOnCompletion (onCompletion)
    }

    fun observeCurrentItem() {
        currentItemLiveData.observeForever(currentItemobserver)
    }

    fun removeObserveCurrentItem() {
        currentItemLiveData.removeObserver(currentItemobserver)
    }


    fun replacePlaylist(playList:List<MediaItem>) {
        this.playList = playList
        cycleNumber.reset(playList.size,playList.indexOf(currentItem))
    }

    fun playItem(playItem:MediaItem) {
        mylog("playItem==>$playItem")
        currentItem = playItem
//        playItem.isPlayedRecent = true
//        launch {
//            dao.update(playItem)
//        }
//        videoItem = VideoItem()
    }

    fun next() {
        currentItem = playList[cycleNumber++.current]
    }

    fun previous() {
        currentItem = playList[cycleNumber--.current]
    }

    fun random() {
        currentItem = playList[cycleNumber.random()]
    }

    fun repeat() {
        currentItem = playList[cycleNumber.repeat()]
    }

    fun seekTo(position: Int) :Int {
        val exactPosition = position.coerceIn(0,currentItem.duration?.toInt()?:0)
        player.seekTo(exactPosition)
        return exactPosition
    }

    fun switchPlayModes() {
        playModes.value = PlayModes.values()[playModeNumber++.current]
        mylog("switchPlayModes==>${playModes.value}")
    }

    fun swithcPlayMode(mode : Int) {
        playModeNumber.current = mode
        playModes.value = PlayModes.values()[playModeNumber.current]
        mylog("switchPlayModes==>${playModes.value}")
    }

    fun play() {
        playStates.value = PlayStates.PLAYING
        player.play()
        launch {
            while(playStates.value == PlayStates.PLAYING) {
                position.postValue(player.getIMediaPlayer().getCurrentPosition())
                dao.update(currentItem) {
                    it.currentPosition = player.getIMediaPlayer().getCurrentPosition().toLong().let {
                        if(currentItem.duration != null && it <= currentItem.duration!!)
                            it
                        else
                            0}
                }
                delay(1000)
            }
        }
    }

    fun pause() {
        playStates.value = PlayStates.PAUSED
        player.pause()
    }

    fun stop() {
        playStates.value = PlayStates.STOPPED
        player.stop()
    }

    fun forward(delta:Int = 3000) : Int {
        return seekTo(position.value?.plus(delta)?:0)
    }

    fun backward(delta: Int = -3000):Int {
        return forward(delta)
    }

    fun unmount() {
        currentItemLiveData.value = null
        stop()
    }

}
enum class PlayModes {SEQUENCE,REPEAT,ALL,RANDOM}