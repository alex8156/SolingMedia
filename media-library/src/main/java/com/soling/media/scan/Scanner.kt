package com.soling.media.scan

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.soling.media.persistence.model.MediaItem
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.*
import java.io.File

/**
 * U盘扫描类
 * File类的walkTopDown扩展方法递归扫描根路径，FilterFile过滤文件类型,clazz创建对应类型（MusicItem、VideoItem、PhotoItem）,MetadataParser解析媒体数据
 *  扫描策略:根据该歌曲路径，查找数据库，有则数据库，没有则解析id3后，再插入数据库,理论上应该可以加快扫描速度
* Created by yizeng on 2018/5/8.
 */
class Scanner<T : MediaItem>(val filterFile: FilterFile, val clazz:Class<T>,val metadataParse : MetadataParser) {

    val scanLiveData = MutableLiveData<List<MediaItem>>()

    private var scannerJob: Deferred<List<MediaItem>>? = null

    fun scan(usbRootPath : String) {
        newScan(usbRootPath,filterFile::filter)
    }

    private fun newScan(usbRootPath : String, filter : (File) -> Boolean) {
        //异步扫描
        scannerJob = async {
            Log.d("MusicListViewModel","List<MusicItem>")
            val list =  mutableListOf<MediaItem>()
             File(usbRootPath).walkTopDown().forEach {
                if(!it.isDirectory && filter(it)) {
                    val mediaItem  = clazz.newInstance()
                    mediaItem.path = it.absolutePath.drop(usbRootPath.length)  //去掉类似/usb/host/Storage01的扫描根路径
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
             scannerJob?.also { scanLiveData.postValue(it.await()) }   //等待扫描完成后再通知数据更新
         }
    }

    fun cancelScan()  = scannerJob?.cancel(CancellationException("扫描被取消"))

}