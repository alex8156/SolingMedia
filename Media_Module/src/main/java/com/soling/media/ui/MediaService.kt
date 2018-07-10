package com.soling.media.ui

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.view.View
import android.widget.Button
import com.soling.media.UsbMusicManager
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
                        UsbMediaViewModelPool.getInstance().mount(path)
                    } else if(action == Intent.ACTION_MEDIA_EJECT) {
                        UsbMediaViewModelPool.getInstance().unmount(path)
                    }
                }
            }
        },filter)
    }


    abstract fun bindManager(): IBinder?

    override fun onBind(intent: Intent?): IBinder? = binder

}