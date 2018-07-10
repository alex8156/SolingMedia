package com.soling.music.ui

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import com.soling.media.persistence.repository.PlayModes
import com.soling.media.player.PlayStates
import com.soling.media.util.mylog
import com.soling.music.R

object PlayBindingAdapters {

    @BindingAdapter("app:playMode")
    @JvmStatic fun playModeChanged(view : Button,playModes: PlayModes?) {
        mylog("playModeChanged==>$playModes")
        playModes?.let {
            when(playModes) {
                PlayModes.SEQUENCE,PlayModes.ALL -> view.setBackgroundResource(R.drawable.selector_bt_sequence)
                PlayModes.REPEAT -> view.setBackgroundResource(R.drawable.selector_bt_repeat)
                PlayModes.RANDOM -> view.setBackgroundResource(R.drawable.selector_bt_random)
            }
        }

    }

    @BindingAdapter("app:playState")
    @JvmStatic fun playStateChanged(view : CompoundButton, playStates: PlayStates?) {
        mylog("playStateChanged==>$playStates;oldChecked==>${view.isChecked}")
        playStates?.let {
            when(playStates) {
                PlayStates.PAUSED, PlayStates.STOPPED-> if(view.isChecked)view.isChecked = false
                PlayStates.PLAYING -> if(!view.isChecked)view.isChecked = true
            }
        }

    }

    @BindingAdapter("app:isFavorite")
    @JvmStatic fun favoriteChanged(view : Button, isFavorite: Boolean) {
        mylog("favoriteChanged==>$isFavorite")
        if(isFavorite)view.setBackgroundResource(R.mipmap.collection_p) else view.setBackgroundResource(R.mipmap.collection)
    }

    @BindingAdapter("app:drawableTop")
    @JvmStatic fun setDrawableTop(view : Button, thumbnail: Bitmap) {
        view.setCompoundDrawables(BitmapDrawable(thumbnail),null,null,null);
    }

    @BindingAdapter("android:layout_height")
    fun setLayoutHeight(view: View, height: Float) {
        val layoutParams = view.getLayoutParams()
        layoutParams.height = height.toInt()
        view.setLayoutParams(layoutParams)
    }

    @BindingAdapter("android:layout_width")
    fun setLayoutWidth(view: View, height: Float) {
        val layoutParams = view.layoutParams
        layoutParams.width = height.toInt()
        view.layoutParams = layoutParams
    }

}