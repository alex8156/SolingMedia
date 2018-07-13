package com.soling.media.scan

import android.media.MediaMetadataRetriever

object MediaMetaRetriever {

    private val mediaMetadataRetriever  = MediaMetadataRetriever()

    fun  setDataSource(path:String) {
            mediaMetadataRetriever.setDataSource(path)
    }

    fun extractMetadata(key : Int) : String ? = mediaMetadataRetriever.extractMetadata(key)

}