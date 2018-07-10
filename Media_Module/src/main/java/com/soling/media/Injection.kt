package com.soling.media

import android.app.Application
import com.soling.media.persistence.repository.MediaDatabase
import com.soling.media.persistence.dao.BaseDao
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.model.PhotoItem
import com.soling.media.persistence.model.VideoItem
import com.soling.media.persistence.repository.InternalMediaListRepository
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.persistence.repository.PlayItemRepository
import com.soling.media.player.AWMediaPlayer
import com.soling.media.player.MusicPlayer
import com.soling.media.player.Singleton
import com.soling.media.scan.*
import com.soling.media.viewmodel.PlayItemViewModelFactory
import com.soling.media.viewmodel.UsbMediaViewModelFactory

object Injection {

    fun usbMusicViewModelFactory(application: Application) =  UsbMediaViewModelFactory.getInstance(
            application, provideUsbMusicListRepository(application)
    )

    fun provideUsbMusicListRepository(application: Application) =  UsbMediaListRepository.getInstance(
            application,
            Scanner(MusicFilter(), MusicItem::class.java, MusicMetadataParser(MediaDatabase.getInstance(application), MediaMetaRetrieverExtracter())),
            MediaDatabase.getInstance(application).musicDao() as BaseDao<Any>
    )

    fun usbVideoViewModelFactory(application: Application) =UsbMediaViewModelFactory.getInstance(application,provideUsbVideoListRepository(application))

    fun provideUsbVideoListRepository(application: Application) =  UsbMediaListRepository.getInstance(
            application,
            Scanner(VideoFilter(), VideoItem::class.java,VideoMetadataParser(MediaDatabase.getInstance(application),MediaMetaRetrieverExtracter())),
            MediaDatabase.getInstance(application).videoDao() as BaseDao<Any>
    )

    fun usbPhotoViewModelFactory(application: Application) =UsbMediaViewModelFactory.getInstance(application,provideUsbPhotoListRepository(application))

    fun provideUsbPhotoListRepository(application: Application) = UsbMediaListRepository.getInstance(
            application,
            Scanner(PhotoFilter(), PhotoItem::class.java,PhotoMetadataParser(MediaDatabase.getInstance(application),MediaMetaRetrieverExtracter())),
            MediaDatabase.getInstance(application).photoDao() as BaseDao<Any>
    )

    fun musicPlayItemViewModelFactory(application: Application) =  PlayItemViewModelFactory.getInstance(application, provideMusicPlayItemRepository(application) )

    fun provideMusicPlayItemRepository(application: Application) = PlayItemRepository.getInstance(MusicPlayer.getInstance(Singleton.getInstance(AWMediaPlayer::class.java)), MediaDatabase.getInstance(application).musicDao() as BaseDao<Any>)

    fun videoPlayItemViewModelFactory(application: Application) = PlayItemViewModelFactory.getInstance(application, provideVideoPlayItemRepository(application))

    fun provideVideoPlayItemRepository(application: Application) = PlayItemRepository.getInstance(MusicPlayer.getInstance(Singleton.getInstance(AWMediaPlayer::class.java)), MediaDatabase.getInstance(application).videoDao() as BaseDao<Any>)


//    fun videoPlayItemViewModelFactory(application: Application) =
//            PlayItemViewModelFactory(application, PlayItemRepository.getInstance(MusicPlayer(AWMediaPlayer()),MediaDatabase.getInstance(application).musicDao() as BaseDao<Any>))


}