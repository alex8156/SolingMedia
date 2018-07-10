package com.soling.media.scan

import android.media.MediaMetadataRetriever
import com.soling.media.util.mylog


class MediaMetaRetrieverExtracter : MetadataExtracter {

    override fun extractMetadata(filePath: String): List<String?>{
        val metaList = mutableListOf<String?>()
        val mediaMetadataRetriever  = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(filePath)
        metaList.add(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
        metaList.add(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM))
        metaList.add( mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        mylog("extractMetadata==>$metaList")
        return metaList.toList()
    }

}