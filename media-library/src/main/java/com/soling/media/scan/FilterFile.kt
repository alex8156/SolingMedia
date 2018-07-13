package com.soling.media.scan

import java.io.File

abstract class FilterFile {
    abstract fun filter(file: File) : Boolean
}