package com.soling.media.persistence.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Trace
import com.soling.media.persistence.dao.BaseDao
import com.soling.media.persistence.model.MediaItem
import com.soling.media.scan.Scanner
import com.soling.media.util.ViewFlags
import com.soling.media.util.mylog
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

/**
 * U盘扫描文件列表数据来源
 * (最初写成单例，后面发现如果有多个列表数据源，比如增加一个U盘数据源,目前想到的解决办法是继承这个类，所以改成open了，感觉这种办法不是很好。
 */
open  class UsbMediaListRepository  constructor(val context: Context, val scanner: Scanner<*>, val dao :BaseDao<Any>)  {

    /**
     * ViewModel观察的MediatorLiveData，所谓的MediatorLiveData是处理两个LiveData的情况：U盘扫描的LiveData和数据库缓存的LiveData
     *
     */
    val usbListLiveData = MediatorLiveData<List<Any?>>() // Model层数据写成Any类型，在View层和ViewModel层转为具体类型

    /**
     * 数据库缓存的LiveData
     */
    var localLiveData: LiveData<out List<Any?>>?= null

    /**
     * 自动播放的LiveData
     */
    var autoPlayLiveData = MediatorLiveData<Any>()
//    val path = MutableLiveData<String>()

    /**
     * 处理有文件，无文件，扫描状态以及行车规制的LiveData
     * flag写法是仿照android源码的标志位机制（戳https://blog.csdn.net/qq_36713816/article/details/53303310），感觉有点繁琐，可能用LiveData分别表示各种状态比较好
     */
    val viewFlagLiveData = MediatorLiveData<Int>().apply { value = 0 }

    /**
     * 扫描的根路径(如 /mnt/usbhost/Strogage01)
     */
    var  usbRootPath :String ? = null

    init {

        usbListLiveData.addSource(scanner.scanLiveData, Observer {
//            usbListLiveData.value = it
//            launch {
//                it?.forEach {
//                    metadataParser.parseMetadata(it)
//                    mylog("$it","parseMetadata");
//                    dao.insert(it)
//                }
////                it?.let { if(it.isNotEmpty())dao.updateAll(*it.toTypedArray() as Array<MusicItem>)}
//            }
            val firstItemJob = launch {
                autoPlayLiveData.postValue(dao.findPlayedRecent()?:dao.findFirst()) //从数据库中找到最近播放的曲目,没有找到播放第一个
            }
            mylog("scan finished==>")
            localLiveData = dao.loadDistinctList(this.usbRootPath) //根据扫描的根路径,查询列表数据
            usbListLiveData.addSource(localLiveData!!,Observer { //加入数据库缓存的LiveData，当数据库增/删/改之后，LiveData自动会更新,Room的机制
                mylog("watch mediaDatabase changed==>${it?.size}")
                val resetFlag = viewFlagLiveData.value!! and (( ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG)).inv()  //清空表示扫描开始和列表非空的标志位
                viewFlagLiveData.value = resetFlag or if(it.orEmpty().isNotEmpty()) ViewFlags.LIST_NOT_EMPTY_FLAG else 0  //如果列表为空,列表非空标志位为0；列表为非空,列表非空标志位为1
//                Trace.endSection()
                launch {
                    firstItemJob.join() // 等待找到最近播放的曲目后，再去更新播放列表
                    usbListLiveData.postValue(it)
                }
            })


        })

    }


    /**
     * 根据扫描根路径，加载数据
     */
     fun loadMusicList(pathName: String) {
        scanner.scan(pathName)
    }

    fun  updateItem (obj : Any) = launch {dao.update(obj)  }

    fun deleteItem(obj: Any) = launch { dao.delete(obj) }

    fun deleteByDisplayName(displayName : String) = launch { dao.deleteByDisplayName(displayName) }

    /**
     * 挂载成功后调用该方法，保存扫描根路径，清空表示U盘存在的标志位，并标为1，然后扫描
     */
    fun mount(usbRootPath: String) {
        mylog("mountPath==>$usbRootPath")
        usbRootPath?.let {
            this.usbRootPath = usbRootPath
//            val resetAllFlag  = viewFlagLiveData.value!! and(ViewFlags.USB_IN_FLAG or ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG or ViewFlags.PARK_ENABLE_FLAG).inv()
             val resetAllFlag  = viewFlagLiveData.value!! and(ViewFlags.USB_IN_FLAG).inv()
            viewFlagLiveData.value = (resetAllFlag or ViewFlags.USB_IN_FLAG)
            mylog("mount==>viewFlag=${viewFlagLiveData.value}")
//            Trace.beginSection("scan")
            loadMusicList(usbRootPath)
        }
    }

    /**
     * U盘开始挂载后调用该方法,清空所有标志位，并把表示开始扫描的标志位设置为1
     */
    fun usbExists() {
        val resetFlag  = viewFlagLiveData.value!! and(ViewFlags.USB_IN_FLAG or ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG or ViewFlags.PARK_ENABLE_FLAG).inv()
        mylog("usbExists==>viewFlag before=${viewFlagLiveData.value}")
        viewFlagLiveData.postValue(resetFlag or ViewFlags.SCAN_START_FLAG)
        mylog("usbExists==>viewFlag after=${viewFlagLiveData.value}")
    }


    /**
     * U盘拔出时调用该方法
     */
    fun unmount() {
        val resetAllFlag  = viewFlagLiveData.value!! and(ViewFlags.USB_IN_FLAG or ViewFlags.SCAN_START_FLAG or ViewFlags.LIST_NOT_EMPTY_FLAG or ViewFlags.PARK_ENABLE_FLAG).inv() //清空所有标志位
        viewFlagLiveData.value = resetAllFlag
        mylog("unmount==>viewFlag=${viewFlagLiveData.value}")
       if(localLiveData!= null) usbListLiveData.removeSource(localLiveData!!)//把数据库LiveData从MediatorLiveData删除,避免退出主界面后再次进入就去查询数据库缓存
        scanner.cancelScan()  //取消扫描
        launch { dao.setAllNotExist(usbRootPath) } //把所用属于该路径下的数据'isExists'设置为0，表示不存在该文件了,插入U盘扫描后会设置为1，表示文件存在
        autoPlayLiveData.value = null  //自动播放的LiveData的值设置为空
//        async { dao.deleteAll() }
//        scanner.scanLiveData.value = listOf()
    }

    /**
     * 行车规制开关会调用该方法,由AutoCore调用
     * 清除该标志位，并根据开关状态标志flag, 1为打开，0为关闭
     */
    fun parkingPlay(enable: Boolean) {
        val resetParkFlag = viewFlagLiveData.value!! and ViewFlags.PARK_ENABLE_FLAG.inv()
        viewFlagLiveData.postValue( resetParkFlag or if(enable) ViewFlags.PARK_ENABLE_FLAG else 0)
        mylog("parkingPlay线程=>${Thread.currentThread().name};parkingPlay=${enable};viewFlag=${viewFlagLiveData.value}")
    }


    companion object {
        @Volatile
        private var INSTANCE: UsbMediaListRepository? = null

        fun getInstance(context: Context, scanner: Scanner<*>, dao :BaseDao<Any>): UsbMediaListRepository = INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                            ?: UsbMediaListRepository(context, scanner, dao).also {
                        INSTANCE = it }
                }


    }


}