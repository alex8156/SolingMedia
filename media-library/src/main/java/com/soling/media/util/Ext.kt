package com.soling.media.util

import android.arch.lifecycle.LiveData
import android.database.Cursor
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.soling.media.persistence.dao.MusicDao
import com.soling.media.persistence.model.MusicItem
import com.soling.media.persistence.repository.UsbMediaListRepository
import java.io.File

inline fun Fragment.transaction(transaction : FragmentTransaction.()-> FragmentTransaction) {
    childFragmentManager.beginTransaction().transaction().commit()
}

inline fun AppCompatActivity.transaction(transaction : FragmentTransaction.()-> FragmentTransaction) {
    supportFragmentManager.beginTransaction().transaction().commit()
}

inline fun Any.mylog(message : String?,tag:String = "musicTag") {
    Log.d(tag,message)
}

inline  fun String.isFileExists() = File(this).exists()


fun <T> Cursor.moveTo(mutableList: MutableList<T>, getT : (Cursor)->T) {
    if(this.count >0) {
        while (this.moveToNext()) {
            mutableList.add(getT(this))
        }
    }
    this.close()
}

fun UsbMediaListRepository.loadFavorite(): LiveData<List<MusicItem>> {
    dao as MusicDao
    return dao.loadFavoriteDistinct()
}


