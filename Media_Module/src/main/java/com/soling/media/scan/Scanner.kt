package com.soling.media.scan

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.soling.media.persistence.model.MediaItem
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.*
import java.io.File

/**
 * Created by yizeng on 2018/5/8.
 */
class Scanner<T : MediaItem>(val filterFile: FilterFile, val clazz:Class<T>,val metadataParse : MetadataParser) {

    val scanLiveData = MutableLiveData<List<MediaItem>>()

    private var scannerJob: Deferred<List<MediaItem>>? = null

    fun scan(usbRootPath : String) {
        newScan(usbRootPath,filterFile::filter)
    }

    private fun newScan(usbRootPath : String, filter : (File) -> Boolean) {
        scannerJob= async {
            Log.d("MusicListViewModel","List<MusicItem>")
            val list =  mutableListOf<MediaItem>()
             File(usbRootPath).walkTopDown().forEach {
                if(!it.isDirectory && filter(it)) {
                    val mediaItem  = clazz.newInstance()
                    mediaItem.path = it.absolutePath.drop(usbRootPath.length)
                    mediaItem.realPath = it.absolutePath
                    mediaItem.displayName = it.name
                    mediaItem.usbRootPath = usbRootPath
                    metadataParse.parseMetadata(mediaItem)
                    list.add(mediaItem)
                }
            }
            list.toList()
        }
         launch {
             scannerJob?.also { scanLiveData.postValue(it.await()) }
             }
    }

    fun cancelScan()  = scannerJob?.cancel(CancellationException("扫描被取消"))

}