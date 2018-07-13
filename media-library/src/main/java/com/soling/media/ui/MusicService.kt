package com.soling.media.ui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.UsbMusicManager
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.persistence.repository.UsbMediaListRepository

class MusicService : MediaService() {


    override fun onCreate() {
        super.onCreate()

    }

    override fun bindManager(): IBinder? {
        return UsbMusicManager(Injection.provideUsbMusicListRepository(MediaApplication.getInstance()),Injection.provideMusicPlayItemRepository(MediaApplication.getInstance()))
    }

}
