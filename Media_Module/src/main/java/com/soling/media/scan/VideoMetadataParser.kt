package com.soling.media.scan

import android.media.ThumbnailUtils
import android.provider.MediaStore
import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.model.VideoItem
import com.soling.media.persistence.repository.MediaDatabase
import com.soling.media.util.mylog

class VideoMetadataParser(mediaDatabase: MediaDatabase, extractMetadata: MetadataExtracter) : MetadataParser(mediaDatabase,extractMetadata) {


     override fun parseMetadata(mediaItem: MediaItem) {
          val localMedial = mediaDatabase.videoDao().loadMedia(mediaItem.path)
          if(mediaItem is VideoItem) {
               if(localMedial == null) {
                    val metalist = super.extractMetadata.extractMetadata(mediaItem.realPath)
                    mediaItem.duration = metalist[2]?.toLong()
                    mediaItem.isExists = true
                    mediaDatabase.videoDao().insert(mediaItem)
                    mylog("from extract==>$mediaItem")
               } else {
                    mediaItem.duration = localMedial.duration
                    mediaItem.isPlayedRecent = localMedial.isPlayedRecent
                    mediaItem.currentPosition = localMedial.currentPosition
                    localMedial.isExists = true
                    mediaItem.isExists = localMedial.isExists
                    mediaDatabase.videoDao().update(mediaItem)
                    mylog("from database==>$mediaItem")
               }
//              try {
//                  mediaItem.thumbnail = ThumbnailUtils.createVideoThumbnail(mediaItem.path, MediaStore.Images.Thumbnails.MICRO_KIND)
//              } catch (e : Exception) {
//                  e.printStackTrace()
//              }
               mylog("thumbnail==>${mediaItem.thumbnail}==>width=${mediaItem.thumbnail?.width?:"null"};height=${mediaItem.thumbnail?.height?:"null"}")
//               Injection.provideFavoriteRepository(MediaApplication.getInstance()).isFavorite(mediaItem)
          }

     }
}