package com.soling.media.scan

import java.io.File

class VideoFilter : FilterFile() {
    override fun filter(file: File): Boolean {
        return file.name.endsWith("mp4")
    }

}