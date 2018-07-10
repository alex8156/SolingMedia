package com.soling.music.ui

import android.app.Application
import com.soling.media.Injection
import com.soling.media.persistence.dao.BaseDao
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.repository.InternalMediaListRepository
import com.soling.media.persistence.repository.MediaDatabase
import com.soling.media.persistence.repository.UsbMediaListRepository
import com.soling.media.scan.MediaMetaRetrieverExtracter
import com.soling.media.scan.MusicFilter
import com.soling.media.scan.MusicMetadataParser
import com.soling.media.scan.Scanner


fun Injection.internalMusicViewModelFactory(application: Application) =  InternalMediaViewModelFactory.getInstance(
        application, provideInternalMusicListRepository(application)
)

fun Injection.provideInternalMusicListRepository(application: Application) =  InternalMediaListRepository.getInstance(
        application,
        Scanner(MusicFilter(), MusicItem::class.java, MusicMetadataParser(MediaDatabase.getInstance(application), MediaMetaRetrieverExtracter())),
        MediaDatabase.getInstance(application).musicDao() as BaseDao<Any>
)
