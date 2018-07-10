package com.soling.media

import android.arch.lifecycle.Observer
import android.graphics.Bitmap
import com.soling.autosdk.source.IAudioStateCallback
import com.soling.autosdk.source.SourceConst
import com.soling.autosdk.source.SourceIntent
import com.soling.autosdk.usb.music.IUsbMusicListener
import com.soling.autosdk.usb.music.UsbMusicInfo
import com.soling.autosdk.usb.music.imp.IUsbMusicManagerImp
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.player.PlayStates

class UsbMusicManager(val usbMediaListRepository: UsbMediaListRepository,val playItemRepository: PlayItemRepository) : IUsbMusicManagerImp() {

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

    override fun getIndex(): Int {
        return playItemRepository.playList.indexOf(playItemRepository.currentItem)
    }

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

    override fun play(modeMemory: Boolean) {
        super.play(modeMemory)
    }

    override fun registerUsbMusicListener(listener: IUsbMusicListener?) {
        super.registerUsbMusicListener(listener)
    }

    override fun hide() {
        super.hide()
    }

    override fun forward(): Int {
        return playItemRepository.forward()
    }

    override fun getMediaScanStatus(): Int {
        return super.getMediaScanStatus()
    }

    override fun onHmiChanged(hmiIndex: Int, down: Boolean) {
        super.onHmiChanged(hmiIndex, down)
    }

    override fun setUsbType(type: Int) {
        super.setUsbType(type)
    }

    override fun getShuffleMode(): Int {
        return super.getShuffleMode()
    }

    override fun unregisterAudioStateListener(listener: IAudioStateCallback?) {
        iAudioStateCallback = null
        playItemRepository.playStates.removeObserver(playStateObserver)
    }

    override fun getRepeatMode(): Int {
        return playItemRepository.playModes.value?.ordinal?:0
    }

    override fun backward(): Int {
        return playItemRepository.backward()
    }

    override fun show() {
        super.show()
    }

    override fun getAllMusicNames(): List<String>?{
        return usbMediaListRepository.usbListLiveData.value?.map { it as MusicItem
        it.displayName
        }
    }

    override fun getTrackName(): String?{
        return playItemRepository.currentItem.displayName?:""
    }

    override fun setShuffleMode(shufflemode: Int) {
        playItemRepository.swithcPlayMode(shuffleMode)
    }

    override fun getMediaMountedCount(): Int {
        return super.getMediaMountedCount()
    }

    override fun getPath(): String {
        return super.getPath()
    }

    override fun stop() {
        super.stop()
    }

    override fun getAlbumName(): String {
        return super.getAlbumName()
    }

    override fun showForResultIntent(sourceIntent: SourceIntent?) {
        super.showForResultIntent(sourceIntent)
    }

    override fun queryByName(name: String?): MutableList<UsbMusicInfo> {
        return super.queryByName(name)
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

    override fun setRepeatMode(repeatmode: Int) {
        playItemRepository.swithcPlayMode(shuffleMode)
    }

    override fun playByName(songName: String?) {
        usbMediaListRepository.usbListLiveData.value?.also {
            it as List<MusicItem>
            it.find { it.displayName == songName }?.
                    also { playItemRepository.currentItem = it
                        playItemRepository.cycleNumber.current = usbMediaListRepository.usbListLiveData.value?.indexOf(it)?:0
                        }
        }
    }

    override fun setTrackRate(isReport: Boolean, milliseconds: Int) {
        super.setTrackRate(isReport, milliseconds)
    }

    override fun getDeviceState(deviceId: Int): Int {
        return super.getDeviceState(deviceId)
    }

    override fun pause() {
        playItemRepository.pause()
    }

    override fun getArtwork(): Bitmap {
        return super.getArtwork()
    }

    override fun reqCurrentPlayName(): String {
        return super.reqCurrentPlayName()
    }

    override fun getPosition(): Long {
        return playItemRepository.position.value?.toLong()?:0L
    }

    override fun getUsbMusicInfo(): UsbMusicInfo {
        return super.getUsbMusicInfo()
    }

    override fun popUpCurrentMode() {
        super.popUpCurrentMode()
    }

    override fun getMusicFileAmount(): Int {
        return super.getMusicFileAmount()
    }

    override fun volumeUp(volume: Float) {
        super.volumeUp(volume)
    }

    override fun setPrevLimitPos(milliseconds: Long) {
        super.setPrevLimitPos(milliseconds)
    }

    override fun getPlayingDevice(): Int {
        return super.getPlayingDevice()
    }

    override fun prev() {
        playItemRepository.previous()
    }

    override fun getQueuePosition(): Int {
        return super.getQueuePosition()
    }

    override fun setQueuePosition(index: Int) {
        super.setQueuePosition(index)
    }

    override fun volumeDown(volume: Float) {
        super.volumeDown(volume)
    }

    override fun registerAudioStateListener(listener: IAudioStateCallback?) {
        iAudioStateCallback = listener
        playItemRepository.playStates.observeForever(playStateObserver)
    }

    override fun getArtistName(): String {
        return playItemRepository.currentItem.let { this as MusicItem
        this.album?:""
        }?:""
    }
}