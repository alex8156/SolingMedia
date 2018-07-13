package com.soling.media.scan

import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.repository.MediaDatabase

class PhotoMetadataParser(mediaDatabase: MediaDatabase, extractMetadata: MetadataExtracter) : MetadataParser(mediaDatabase,extractMetadata) {



     override fun parseMetadata(mediaItem: MediaItem) {
     }
}