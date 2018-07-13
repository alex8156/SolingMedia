package com.soling.media.util

/**
 * Created by yizeng on 2017/6/5.
 */

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager

import java.io.FileOutputStream


object ScreenUtil {

    /** 获取屏幕密度  */
    fun getScreenDensity(context: Context): Float {
        return getDisplayMetrics(context).density
    }

    /** 获取屏幕宽度(像素)  */
    fun getScreenWidthPixels(context: Context): Int {
        return getDisplayMetrics(context).widthPixels
    }

    /** 获取屏幕宽度(dp)  */
    fun getScreenWidthDp(context: Context): Float {
        val displayMetrics = getDisplayMetrics(context)
        return displayMetrics.widthPixels / displayMetrics.density
    }

    /** 获取屏幕高度(像素)  */
    fun getScreenHeightPixels(context: Context): Int {
        return getDisplayMetrics(context).heightPixels
    }

    /** 获取屏幕高度(dp)  */
    fun getScreenHeightDp(context: Context): Float {
        val displayMetrics = getDisplayMetrics(context)
        return displayMetrics.heightPixels / displayMetrics.density
    }

    /** 获取状态栏高度  */
    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /**
     * 保存屏幕截图到本地
     *
     * @param activity
     * @param strFileName 文件全路径:例如 "/sdcard/screen_shot_20160424.jpg"
     */
    fun savScreenShot(activity: Activity, strFileName: String) {
        val takeShot = takeShot(activity)
        savePic(takeShot, strFileName)
    }

    /**
     * 截图
     * 也可以调用shell命令去截图  screencap -p test.png
     *
     * @param activity 截取activity 所在的页面的截图,即使退到后台也是截取这个activity
     */
    private fun takeShot(activity: Activity): Bitmap {
        // 获取windows中最顶层的view
        val view = activity.window.decorView
        // 允许当前窗口保存缓存信息
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()

        //状态栏高度
        val outRect = Rect()
        view.getWindowVisibleDisplayFrame(outRect)
        var statusBarHeight = outRect.top

        //状态栏+标题栏目的高度
        statusBarHeight = activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).top

        //屏幕宽高
        val height = getScreenHeightPixels(activity)
        val width = getScreenWidthPixels(activity)

        // 如果需要状态栏,则使用 Bitmap b = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, width, height - statusBarHeight);
        val b = Bitmap.createBitmap(view.drawingCache, 0, statusBarHeight, width, height - statusBarHeight)
        // 销毁缓存信息
        view.destroyDrawingCache()

        return b
    }


    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val outMetrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(outMetrics)
        return outMetrics
    }


    // 保存到sdcard
    private fun savePic(b: Bitmap, strFileName: String) {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(strFileName)
            if (null != fos) {
                // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                b.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.flush()
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
