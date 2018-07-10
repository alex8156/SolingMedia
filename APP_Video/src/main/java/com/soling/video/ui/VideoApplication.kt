package com.soling.video.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.soling.autosdk.usb.UsbConst
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.util.mylog


class VideoApplication : MediaApplication() {

    override fun onCreate() {
        super.onCreate()
        mylog("VideoApplication onCreate")
        Injection.usbVideoViewModelFactory(this)
        Injection.videoPlayItemViewModelFactory(this)
        bindService(Intent( UsbConst.USB_VIDEO_SERVICE),object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            }

        }, Context.BIND_AUTO_CREATE)

    }
}