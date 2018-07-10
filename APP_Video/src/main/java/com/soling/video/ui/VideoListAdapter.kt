package com.soling.music.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.soling.media.persistence.model.VideoItem
import com.soling.video.R
import com.soling.video.databinding.ItemVideoBinding


class VideoListAdapter(private val context: Context?, private val onBind: (MusicVH?, List<VideoItem>) -> Unit) : RecyclerView.Adapter<MusicVH>() {

    private var list = listOf<VideoItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicVH {
        val itemMusicBinding = DataBindingUtil.inflate<ItemVideoBinding>(LayoutInflater.from(context), R.layout.item_video, parent, false);
        return MusicVH(itemMusicBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicVH, position: Int) {
        holder?.bind(list[position])
        onBind(holder,list)
    }

    fun replaceData(newValue: List<VideoItem>?) {
        newValue?.let {
            list = newValue
            notifyDataSetChanged()
        }

    }

}

class MusicVH(val itemVideoBinding: ItemVideoBinding) : RecyclerView.ViewHolder(itemVideoBinding.root) {

    fun bind(item: VideoItem) {
        itemVideoBinding.item = item
        itemVideoBinding.executePendingBindings()
    }

}