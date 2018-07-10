package com.soling.media.scan

import java.io.File

class PhotoFilter : FilterFile() {
    override fun filter(file: File): Boolean {
        return file.name.endsWith("jpg")
    }

}