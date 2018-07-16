package com.soling.media.scan


/**
 * 媒体元数据解析类
 */
interface MetadataExtracter {
    fun extractMetadata(filePath : String):List<String?>
}