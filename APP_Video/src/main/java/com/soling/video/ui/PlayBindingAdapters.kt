package com.soling.music.ui

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import com.soling.media.persistence.repository.PlayModes
import com.soling.media.player.PlayStates
import com.soling.media.util.mylog
import com.soling.video.R

object PlayBindingAdapters {

    @BindingAdapter("app:playMode")
    @JvmStatic fun playModeChanged(view : Button,playModes: PlayModes?) {
        mylog("playModeChanged==>$playModes")
        playModes?.let {
            when(playModes) {
                PlayModes.SEQUENCE,PlayModes.ALL -> view.setBackgroundResource(R.drawable.selector_bt_sequence_v)
                PlayModes.REPEAT -> view.setBackgroundResource(R.drawable.selector_bt_repeat_v)
                PlayModes.RANDOM -> view.setBackgroundResource(R.drawable.selector_bt_random_v)
            }
        }

    }

    @BindingAdapter("app:playState")
    @JvmStatic fun playStateChanged(view : CompoundButton, playStates: PlayStates?) {
        mylog("playStateChanged==>$playStates;oldChecked==>${view.isChecked}")
        when(playStates) {
            PlayStates.PAUSED, PlayStates.STOPPED-> if(view.isChecked)view.isChecked = false
            PlayStates.PLAYING -> if(!view.isChecked)view.isChecked = true
        }

    }


    @BindingAdapter("app:imageBitmap")
    @JvmStatic fun setBitmap(view : ImageView, thumbnail: Bitmap?) {
        if(thumbnail != null )view.setImageBitmap(thumbnail)
        else view.setImageDrawable(view.resources.getDrawable(R.mipmap.video_noinformation))
    }

//    @BindingAdapter("android:layout_height")
//    fun setLayoutHeight(view: View, height: Float) {
//        val layoutParams = view.layoutParams
//        layoutParams.height = height.toInt()
//        view.layoutParams = layoutParams
//    }
//
//    @BindingAdapter("android:layout_width")
//    fun setLayoutWidth(view: View, height: Float) {
//        val layoutParams = view.layoutParams
//        layoutParams.width = height.toInt()
//        view.layoutParams = layoutParams
//    }

}