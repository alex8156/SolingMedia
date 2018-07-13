package com.soling.media.persistence.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore

/**
 * Created by yizeng on 2018/5/9.
 */
@Entity
open class MusicItem : MediaItem(){

    @ColumnInfo  var album:String? = null

    @ColumnInfo  open var artist : String? = null

    @ColumnInfo
    var isFavorite : Boolean   = false

    override fun toString(): String {
        return "MusicItem(path='$path', displayName='$displayName', artist=$artist, album=$album, duration=$duration, isFavorite=$isFavorite)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MusicItem

        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isFavorite.hashCode()
        return result
    }


}