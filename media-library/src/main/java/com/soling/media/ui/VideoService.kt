package com.soling.media.ui

import android.os.IBinder
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.VideoMusicManager

class VideoService : MediaService() {

    override fun bindManager(): IBinder? {
        return VideoMusicManager(Injection.provideUsbVideoListRepository(MediaApplication.getInstance()),Injection.provideVideoPlayItemRepository(MediaApplication.getInstance()))
    }

}