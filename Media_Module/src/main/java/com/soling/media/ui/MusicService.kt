package com.soling.media.ui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.UsbMusicManager

class MusicService : MediaService() {


    override fun bindManager(): IBinder? {
        return UsbMusicManager(Injection.provideUsbMusicListRepository(MediaApplication.getInstance()),Injection.provideMusicPlayItemRepository(MediaApplication.getInstance()))
    }

}