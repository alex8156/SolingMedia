package com.soling.media.util

import java.util.*

/**
 * Created by alex on 2018/5/15.
 */
class CycleNumber(var min : Int, var max: Int = min

) {
    var current : Int = min
     set(value) { field = value.coerceIn(min,max)%max }

    fun reset(max : Int,current: Int) {
        this.max = max
        this.current = current
    }

    val random : Random by lazy {
        Random()
    }

    fun next() : Int {
        current=(++current).coerceAtMost(max)%max
        return current
    }

    fun previous() : Int {
        current = (--current).coerceAtLeast(min)%max
        return current
    }

    fun random() : Int {
        current = random.nextInt(max) + min
        return current
    }

    fun repeat(): Int = current



    operator fun inc() : CycleNumber {
        next()
        return this
    }


    operator fun dec() : CycleNumber {
        previous()
        return this
    }
}