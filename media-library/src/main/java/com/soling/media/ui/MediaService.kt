package com.soling.media.ui

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.os.IBinder
import android.view.View
import android.widget.Button
import com.soling.autosdk.os.Soc
import com.soling.autosdk.os.SocConst
import com.soling.autosdk.os.imp.SocListenerImp
import com.soling.media.Injection
import com.soling.media.UsbMusicManager
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.util.AutoSdkEx
import com.soling.media.util.mylog
import com.soling.media.viewmodel.UsbMediaViewModelPool

abstract class MediaService : Service() {

    private var binder : IBinder? = null

    override fun onCreate() {
        super.onCreate()
        scanStrategy()
        if (binder == null) binder = bindManager()
    }

    open fun scanStrategy() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_MEDIA_MOUNTED)
            addAction(Intent.ACTION_MEDIA_UNMOUNTED)
            addAction(Intent.ACTION_MEDIA_BAD_REMOVAL)
            addAction(Intent.ACTION_MEDIA_EJECT)
            addAction(Intent.ACTION_MEDIA_CHECKING)
            addAction(Intent.ACTION_MEDIA_REMOVED)
            addAction(Intent.ACTION_MEDIA_UNMOUNTABLE)
            addDataScheme("file")
//            addAction(SocConst.ACTION_DEVICE_LOST);
        }
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val action = intent.action
                val path = intent.data?.toString()?.substring("file://".length)
                mylog("action:$action, path:$path")
                if(path != null && path.contains("/mnt/usbhost/")) {
                    if (action == Intent.ACTION_MEDIA_MOUNTED) {
                          (binder as RepositoryGetter).getReposotoryPair().first.mount(path)
//                        UsbMediaViewModelPool.getInstance().mount(path)
                    } else if(action == Intent.ACTION_MEDIA_EJECT) {
                        (binder as RepositoryGetter).getReposotoryPair().first.unmount()
                        (binder as RepositoryGetter).getReposotoryPair().second.unmount()
//                        UsbMediaViewModelPool.getInstance().unmount(path)
                    }
                }
            }
        },filter)
        Soc.getInstance().registerListener(object :SocListenerImp(){
            override fun onUsbDeviceChanged(index: Int) {
                mylog("onUsbDeviceChanged==>$index")
                when(index) {
                    SocConst.UsbDevices.UDISK.ordinal ->
                        if(Soc.getInstance().isUsbDeviceExist(SocConst.UsbDevices.UDISK)) {
                            if(binder is RepositoryGetter) {
                                (binder as RepositoryGetter).getReposotoryPair().first.usbExists()
                                mylog("usbDeviceExist")
                            }
                        } else {
                            mylog("usbDeviceNotExist")
                        }
                }
            }
        })
    }


    abstract fun bindManager(): IBinder?

    override fun onBind(intent: Intent?): IBinder? = binder


}

interface RepositoryGetter {
    fun getReposotoryPair() : Pair<UsbMediaListRepository, PlayItemRepository>
}