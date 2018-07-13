package com.soling.media.scan

import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.repository.MediaDatabase

abstract class MetadataParser(open val mediaDatabase: MediaDatabase,val extractMetadata: MetadataExtracter) {
     abstract fun parseMetadata(mediaItem: MediaItem)
}