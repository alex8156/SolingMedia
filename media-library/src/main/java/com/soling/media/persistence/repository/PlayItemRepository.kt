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

/**
 * 播放单条曲目Model层
 */
class PlayItemRepository private constructor(val  player : Player, val dao: BaseDao<Any>) {

    /**
     * 播放列表
     */
    var playList = listOf<MediaItem>()

    /**
     * 该播放列表中当前正在播放的曲目，使用观察委托，是为了把新老数据中的最近播放字段设置为0和1
     */
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
           currentItemLiveData.value = new  //更新当前播放曲目值
    }

     fun BaseDao<Any>.update(mediaItem: MediaItem,propertyChange:(MediaItem)->Unit) {
         launch {
             propertyChange(mediaItem)
             dao.update(mediaItem)
         }
    }


    val currentItemLiveData = MutableLiveData<MediaItem>()  //当前播放的LiveData

    var cycleNumber : CycleNumber = CycleNumber(0);  //播放"索引"

    var playModeNumber : CycleNumber = CycleNumber(0, PlayModes.values().size) //播放模式"索引"

    val position = MutableLiveData<Int>()  //播放位置LiveData

    val playStates =  MutableLiveData<PlayStates>() //播放状态LiveData

    val playModes = MutableLiveData<PlayModes>().apply { value = PlayModes.SEQUENCE }  //播放模式LiveData ,默认顺序播放


    /**
     * 当播放曲目变化时,会调用该方法
     *
     */
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


    /**
     * 播放完成后的回调
     */
    val onCompletion = {
        mylog("onCompletion==>${playModes.value}")
        if(playList.isNotEmpty()) {
            pause()
            dao.update(currentItem) {  //进度重新设置为0
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

    /**
     * 观察当前播放LiveData
     * 音乐需要后台观察，所以初始化PlayItemRepository时就调用了该方法;视频不需要后台观察,所以在SurfaceView创建了之后才去观察
     */
    fun observeCurrentItem() {
        currentItemLiveData.observeForever(currentItemobserver)
    }

    /**
     * 移除当前播放的LiveData ,音乐不需要移除,视频在SurfaceView销毁后移除观察
     */
    fun removeObserveCurrentItem() {
        currentItemLiveData.removeObserver(currentItemobserver)
    }


    /**
     * 切换播放列表
     */
    fun replacePlaylist(playList:List<MediaItem>) {
        this.playList = playList
        cycleNumber.reset(playList.size,playList.indexOf(currentItem))
    }

    /**
     * 播放单条曲目
     */
    fun playItem(playItem:MediaItem) {
        mylog("playItem==>$playItem")
        currentItem = playItem
//        playItem.isPlayedRecent = true
//        launch {
//            dao.update(playItem)
//        }
//        videoItem = VideoItem()
    }

    /**
     * 播放下一首
     */
    fun next() {
        currentItem = playList[cycleNumber++.current]
    }

    /**
     * 播放上一首
     */
    fun previous() {
        currentItem = playList[cycleNumber--.current]
    }

    /**
     * 播放下一首
     */
    fun random() {
        currentItem = playList[cycleNumber.random()]
    }

    /**
     *单一播放
     */
    fun repeat() {
        currentItem = playList[cycleNumber.repeat()]
    }

    fun seekTo(position: Int) :Int {
        val exactPosition = position.coerceIn(0,currentItem.duration?.toInt()?:0)
        player.seekTo(exactPosition)
        return exactPosition
    }

    /**
     * 切换播放模式
     */
    fun switchPlayModes() {
        playModes.value = PlayModes.values()[playModeNumber++.current]
        mylog("switchPlayModes==>${playModes.value}")
    }

    /**
     * 切换到某种播放模式
     */
    fun swithcPlayMode(mode : Int) {
        playModeNumber.current = mode
        playModes.value = PlayModes.values()[playModeNumber.current]
        mylog("switchPlayModes==>${playModes.value}")
    }

    /**
     * 播放
     * 当播放时,新开线程，每隔一秒更新进度,并保存数据到数据库，虽然使切换曲目时和插入U盘时得以记忆上次的位置，提高用户体验,但会影响性能
     */
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

    /**
     * 暂停
     */
    fun pause() {
        playStates.value = PlayStates.PAUSED
        player.pause()
    }

    /**
     * 停止
     */
    fun stop() {
        playStates.value = PlayStates.STOPPED
        player.stop()
    }

    /**
     * 快进
     */
    fun forward(delta:Int = 3000) : Int {
        return seekTo(position.value?.plus(delta)?:0)
    }

    /**
     * 后退
     */
    fun backward(delta: Int = -3000):Int {
        return forward(delta)
    }

    /**
     * U盘卸载时会调用该方法
     *
     */
    fun unmount() {
        currentItemLiveData.value = null  //设置当前播放值为空
        stop()  //停止播放
    }

}
enum class PlayModes {SEQUENCE,REPEAT,ALL,RANDOM}