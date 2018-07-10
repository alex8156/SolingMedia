package com.soling.media.viewmodel

import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.util.ViewFlags
import com.soling.media.util.mylog

class UsbMediaViewModelPool {

    private val usbMediaViewModels = mutableListOf<UsbMediaViewModel>()

    fun mount(mountPath : String) {
        mylog("mountPath==>$mountPath")
        usbMediaViewModels.filter {
            it.mediaListRepository.viewFlagLiveData.value?.and(ViewFlags.USB_IN_FLAG).let {
                mylog("viewFlagLiveData.value=>${it?.toString(16)}")
            it == null || it == 0
        }}.firstOrNull()?.let { it.mediaListRepository?.mount(mountPath)
                                it.mountFlagLiveData.value = true
        }
    }

    fun unmount(unmountPath : String) {
        mylog("unmountPath==>$unmountPath")
        usbMediaViewModels.filter {  unmountPath == it.mediaListRepository.usbRootPath }.firstOrNull()?.let {
            it.mediaListRepository.unmount()
            it.mountFlagLiveData.value = false
        }
        Injection.provideVideoPlayItemRepository(MediaApplication.getInstance()).unmount()
        Injection.provideMusicPlayItemRepository(MediaApplication.getInstance()).unmount()
    }

    fun add(viewModel: UsbMediaViewModel) {
        usbMediaViewModels.add(viewModel)
    }

    fun clear() {
        usbMediaViewModels.clear()
    }

    companion object {
        @Volatile
        private var INSTANCE: UsbMediaViewModelPool? = null

        fun getInstance(): UsbMediaViewModelPool = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: UsbMediaViewModelPool().also {
                        INSTANCE = it }
         }

    }



}