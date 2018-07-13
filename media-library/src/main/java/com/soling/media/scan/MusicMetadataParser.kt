package com.soling.media.scan

import com.soling.media.MediaApplication
import com.soling.media.Injection
import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.repository.MediaDatabase
import com.soling.media.util.mylog

class MusicMetadataParser (mediaDatabase: MediaDatabase, extractMetadata: MetadataExtracter): MetadataParser(mediaDatabase,extractMetadata) {


     override fun parseMetadata(mediaItem: MediaItem) {
           val localMedial = mediaDatabase.musicDao().loadMedia(mediaItem.path)
          if(mediaItem is MusicItem) {
               if(localMedial == null) {
                    val metalist = super.extractMetadata.extractMetadata(mediaItem.realPath)
                    mediaItem.album = metalist[0]
                    mediaItem.artist = metalist[1]
                    mediaItem.duration = metalist[2]?.toLong()
                    mediaItem.isExists = true
                    mediaDatabase.musicDao().insert(mediaItem)
                    mylog("from extract==>$mediaItem")
               } else {
                    mediaItem.album = localMedial.album
                    mediaItem.artist = localMedial.artist
                    mediaItem.duration = localMedial.duration
                    mediaItem.isPlayedRecent = localMedial.isPlayedRecent
                    mediaItem.currentPosition = localMedial.currentPosition
                    localMedial.isExists = true
                    mediaItem.isExists = localMedial.isExists
                    mediaItem.isFavorite = localMedial.isFavorite
                    mediaDatabase.musicDao().update(mediaItem)
                    mylog("from database==>$mediaItem")
               }
//               Injection.provideFavoriteRepository(MediaApplication.getInstance()).isFavorite(mediaItem)
          }

     }
}