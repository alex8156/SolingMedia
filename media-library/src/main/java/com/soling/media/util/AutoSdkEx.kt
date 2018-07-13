package com.soling.media.util

import com.soling.autosdk.os.Soc
import com.soling.autosdk.source.Source
import com.soling.autosdk.source.SourceConst

inline fun Any.musicAppChanged(active: Boolean) {
    AutoSdkEx.source.mainAppChanged(SourceConst.App.USB_MUSIC,active)
}

inline fun Any.videoAppChanged(active: Boolean) {
    AutoSdkEx.source.mainAppChanged(SourceConst.App.USB_VIDEO,active)
}


object AutoSdkEx {
    val source = Source()
}
