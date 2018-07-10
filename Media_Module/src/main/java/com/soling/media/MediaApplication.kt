package com.soling.media

import android.app.Application

/**
 * Created by yizeng on 2018/5/9.
 */
open class MediaApplication : Application() {

    companion object {
        private var application : MediaApplication ? = null
        fun getInstance() : MediaApplication = application!!
    }


    override fun onCreate() {
        super.onCreate()
        application = this
    }



}