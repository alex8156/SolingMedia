package com.soling.media

import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import com.soling.autosdk.source.IAudioStateCallback
import com.soling.autosdk.source.SourceConst
import com.soling.autosdk.source.SourceIntent
import com.soling.autosdk.usb.music.IUsbMusicListener
import com.soling.autosdk.usb.music.UsbMusicInfo
import com.soling.autosdk.usb.music.imp.IUsbMusicManagerImp
import com.soling.autosdk.usb.video.imp.IUsbVideoManagerImp
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.player.PlayStates
import com.soling.media.ui.RepositoryGetter

class VideoMusicManager(val usbMediaListRepository: UsbMediaListRepository, val playItemRepository: PlayItemRepository) : IUsbVideoManagerImp(),RepositoryGetter {

    var iAudioStateCallback : IAudioStateCallback? = null

    val playStateObserver = object : Observer<PlayStates> {
        override fun onChanged(playStates: PlayStates?) {
            when(playStates) {
                PlayStates.PLAYING -> iAudioStateCallback?.onAudioStateChanged(SourceConst.AudioState.AUDIO_STATE_PLAYING)
                PlayStates.PAUSED -> iAudioStateCallback?.onAudioStateChanged(SourceConst.AudioState.AUDIO_STATE_PAUSED)
                PlayStates.STOPPED ->iAudioStateCallback?.onAudioStateChanged(SourceConst.AudioState.AUDIO_STATE_STOPED)
            }
        }
    }

//    override fun getIndex(): Int {
//        return playItemRepository.playList.indexOf(playItemRepository.currentItem.value)
//    }

    override fun getDuration(): Long {
        return playItemRepository.currentItem.duration?:0
    }

    override fun showForResult(para: Int) {
        super.showForResult(para)
    }

    override fun seek(pos: Long): Long {
        playItemRepository.currentItem?.let { playItemRepository.seekTo(pos.toInt()) }
        return pos
    }

    override fun getDeviceName(): String {
        return super.getDeviceName()
    }



    override fun hide() {
        super.hide()
    }

    override fun forward(): Int {
        return playItemRepository.forward()
    }


    override fun onHmiChanged(hmiIndex: Int, down: Boolean) {
        super.onHmiChanged(hmiIndex, down)
    }



    override fun unregisterAudioStateListener(listener: IAudioStateCallback?) {
        iAudioStateCallback = null
        playItemRepository.playStates.removeObserver(playStateObserver)
    }

    override fun backward(): Int {
        return playItemRepository.backward()
    }

    override fun show() {
        super.show()
    }

    override fun getAllVideoNames(): List<String>?{
        return usbMediaListRepository.usbListLiveData.value?.map { it as MusicItem
        it.displayName
        }
    }



    override fun stop() {
        super.stop()
    }


    override fun showForResultIntent(sourceIntent: SourceIntent?) {
        super.showForResultIntent(sourceIntent)
    }


    override fun getPlayState(): Int {
        return super.getPlayState()
    }

    override fun isConnected(): Boolean {
        return super.isConnected()
    }

    override fun next() {
        super.next()
    }


    override fun playByName(songName: String?) {
        usbMediaListRepository.usbListLiveData.value?.also {
            it as List<MusicItem>
            it.find { it.displayName == songName }?.
                    also { playItemRepository.currentItem=it
                        playItemRepository.cycleNumber.current = usbMediaListRepository.usbListLiveData.value?.indexOf(it)?:0
                        }
        }
    }


    override fun pause() {
        playItemRepository.pause()
    }

    override fun getArtwork(): Bitmap {
        return super.getArtwork()
    }


    override fun getPosition(): Long {
        return playItemRepository.position.value?.toLong()?:0L
    }



    override fun volumeUp(volume: Float) {
        super.volumeUp(volume)
    }


    override fun getPlayingDevice(): Int {
        return super.getPlayingDevice()
    }

    override fun prev() {
        playItemRepository.previous()
    }


    override fun volumeDown(volume: Float) {
        super.volumeDown(volume)
    }

    override fun registerAudioStateListener(listener: IAudioStateCallback?) {
        iAudioStateCallback = listener
        playItemRepository.playStates.observeForever(playStateObserver)
    }

    override fun ParkingPlay(isEnable: Boolean) {
        usbMediaListRepository.parkingPlay(isEnable)
    }

    override fun getReposotoryPair(): Pair<UsbMediaListRepository, PlayItemRepository> {
        return Pair(usbMediaListRepository,playItemRepository)
    }


}