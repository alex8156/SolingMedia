package com.soling.media.scan


interface MetadataExtracter {
    fun extractMetadata(filePath : String):List<String?>
}