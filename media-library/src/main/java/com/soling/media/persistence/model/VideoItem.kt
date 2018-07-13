package com.soling.media.persistence.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.graphics.Bitmap

/**
 * Created by yizeng on 2018/5/9.
 */
@Entity
open class VideoItem : MediaItem() {
   @Ignore var thumbnail :Bitmap ? = null

   override fun toString(): String {
      return "VideoItem(path='$path', displayName='$displayName', duration=$duration, currentPosition=$currentPosition, isPlayedRecent=$isPlayedRecent,realPath='$realPath', usbRootPath='$usbRootPath')"
   }

}