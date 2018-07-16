package com.soling.media.util

import java.util.*

/**
 * 循环一串数字序列，比如012340123401234...
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

    //下一个数字
    fun next() : Int {
        current=(++current).coerceAtMost(max)%max
        return current
    }

    //上一个数字
    fun previous() : Int {
        current = (--current).coerceAtLeast(min)%max
        return current
    }

    //随机数字
    fun random() : Int {
        current = random.nextInt(max) + min
        return current
    }

    //单一数字
    fun repeat(): Int = current


    //重载自加操作号
    operator fun inc() : CycleNumber {
        next()
        return this
    }

    //重载自减操作符号
    operator fun dec() : CycleNumber {
        previous()
        return this
    }
}