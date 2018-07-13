package com.soling.media.player

class Singleton {


    companion object {
        @Volatile
        private var INSTANCE: IMediaPlayer? = null

        fun <T : IMediaPlayer> getInstance(clazz: Class<T>): IMediaPlayer = INSTANCE
                ?: synchronized(clazz) {
                    INSTANCE
                            ?: clazz.newInstance().also {
                                INSTANCE = it }
                }
    }


}