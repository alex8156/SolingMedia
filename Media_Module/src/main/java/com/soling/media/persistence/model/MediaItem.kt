package com.soling.media.persistence.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by yizeng on 2018/5/7.
 */
@Entity
open class MediaItem(@PrimaryKey open var path  :  String = "") {

    @ColumnInfo open  var displayName : String = ""
    @ColumnInfo open  var duration : Long ?= null

    @ColumnInfo var currentPosition:Long = 0

    @ColumnInfo var isPlayedRecent : Boolean = false

    @ColumnInfo var realPath : String = ""

    @ColumnInfo var usbRootPath :String = ""

    @ColumnInfo var isExists = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaItem

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun toString(): String {
        return "MediaItem(path='$path', displayName='$displayName', duration=$duration, currentPosition=$currentPosition, isPlayedRecent=$isPlayedRecent, realPath='$realPath', usbRootPath='$usbRootPath')"
    }


}
