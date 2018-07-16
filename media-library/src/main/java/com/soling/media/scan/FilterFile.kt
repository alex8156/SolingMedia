package com.soling.media.scan

import java.io.File

/**
 * 过滤文件类
 */
abstract class FilterFile {
    abstract fun filter(file: File) : Boolean
}