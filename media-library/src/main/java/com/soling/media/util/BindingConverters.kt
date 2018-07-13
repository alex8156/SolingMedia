/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.soling.media.util

import android.databinding.BindingConversion
import android.view.View
import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.model.MusicItem
import java.text.SimpleDateFormat
import java.util.*


/**
 * The number of likes is an integer and the visibility attribute takes an integer
 * (VISIBLE, GONE and INVISIBLE are 0, 4 and 8 respectively), so we use this converter.
 *
 * There is no need to specify that this converter should be used. [BindingConversion]s are
 * applied automatically.
 */
object BindingConverters{

    @BindingConversion
    @JvmStatic fun booleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }
}

object ConverterUtil {

    val simpleDateFormat = SimpleDateFormat("mm:ss")
    val date = Date()

    @JvmStatic fun formatTime(time: Long): String {
        date.time = time
        return  simpleDateFormat.format(date)
    }
    @JvmStatic fun cast(item: MediaItem): MusicItem {
        return   item as MusicItem
    }



}


