package com.soling.music.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.soling.media.persistence.model.MusicItem
import com.soling.music.R
import com.soling.music.databinding.ItemMusicBinding


class MusicListAdapter(private val context: Context?, private val onBind: (MusicVH?, List<MusicItem>) -> Unit) : RecyclerView.Adapter<MusicVH>() {

    private var list = listOf<MusicItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicVH {
        val itemMusicBinding = DataBindingUtil.inflate<ItemMusicBinding>(LayoutInflater.from(context), R.layout.item_music, parent, false);
        return MusicVH(itemMusicBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicVH, position: Int) {
        holder?.bind(list[position])
        onBind(holder,list)
    }

    fun replaceData(newValue: List<MusicItem>?) {
        newValue?.let {
            list = newValue
            notifyDataSetChanged()
        }

    }

}

class MusicVH(val itemMusicBinding: ItemMusicBinding) : RecyclerView.ViewHolder(itemMusicBinding.root) {

    fun bind(item: MusicItem) {
        itemMusicBinding.item = item
        itemMusicBinding.executePendingBindings()
    }

}