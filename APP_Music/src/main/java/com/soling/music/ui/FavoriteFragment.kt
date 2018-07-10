package com.soling.music.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.music.R
import com.soling.music.databinding.ChildFragmentFavoritelistBinding
import com.soling.music.databinding.ItemFavoriteBinding
import com.soling.media.persistence.model.MusicItem
import com.soling.media.viewmodel.NewPlayItemViewModel
import com.soling.media.viewmodel.UsbMediaViewModel

class FavoriteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val childFragmentFavoritelistBinding = DataBindingUtil.inflate<ChildFragmentFavoritelistBinding>(inflater, R.layout.child_fragment_favoritelist,container,false);
        val recycelView = childFragmentFavoritelistBinding.recycelView
        val playViewmodel = ViewModelProviders.of(activity!!, Injection.musicPlayItemViewModelFactory(MediaApplication.getInstance())).get(NewPlayItemViewModel::class.java)
        val favoriteListViewModel = ViewModelProviders.of(activity!!,null).get(FavoriteListViewModel::class.java)
        val usbMediaViewModel = ViewModelProviders.of(activity!!, Injection.usbMusicViewModelFactory(MediaApplication.getInstance())).get(UsbMediaViewModel::class.java)
        recycelView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        val adpater = FavoriteListAdapter(activity?.applicationContext, { holder ->
            holder?.itemFavoriteBinding?.root?.setOnClickListener({
                (activity as MainActivity).showPlayFragment()
                playViewmodel.playItem(holder.itemFavoriteBinding.item!!)
                playViewmodel.replaceList(favoriteListViewModel.favoriteMusicList.value as List<MusicItem>)
            })
            holder?.itemFavoriteBinding?.btDelete?.setOnClickListener({
                val musicItem = holder!!.itemFavoriteBinding.item!!.also { it.isFavorite = false }
                usbMediaViewModel.updateItem(musicItem)
            })
        })
        recycelView.adapter = adpater
//        MediaApplication.getInstance().musicListViewModel.usbLiveData.observe(
//                this, Observer<List<MusicItem>> { adpater.replaceData(it) })
        favoriteListViewModel.favoriteMusicList.observe(this, Observer {
            adpater.replaceData(it)
        })
        childFragmentFavoritelistBinding.tvFlag.setText("收藏");
//        recycel_view.layoutManager = LinearLayoutManager(activity?.applicationContext)
//        val typeItemm = Type<ItemFavoriteBinding>(R.layout.item_music)
//                .onBind {  }
//                .onClick {
//                    val list = listViewModel.favoriteMusicList.value
//                    if(list != null)
//                        it.binding.item?.also {
//                        playViewmodel.onListChanged(list,it)}
//       }
//
//        LastAdapter(listViewModel.favoriteMusicList.value!!, BR.item).type {
//            item, position -> typeItemm
//        }.layout { item, position ->
//            R.layout.item_music
//        }.into(recycel_view)

        return childFragmentFavoritelistBinding.root
    }


    class FavoriteListAdapter(private val context: Context?, private val onBind: (MusicVH?) -> Unit) : RecyclerView.Adapter<MusicVH>() {

        private var list = listOf<MusicItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicVH {
            val itemMusicBinding = DataBindingUtil.inflate<ItemFavoriteBinding>(LayoutInflater.from(context), R.layout.item_favorite, parent, false);
            return MusicVH(itemMusicBinding)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: MusicVH, position: Int) {
            holder?.bind(list[position])
            onBind(holder)
        }

        fun replaceData(newValue: List<MusicItem>?) {
            newValue?.let {
                list = newValue
                notifyDataSetChanged()
            }

        }

    }

    class MusicVH(val itemFavoriteBinding: ItemFavoriteBinding) : RecyclerView.ViewHolder(itemFavoriteBinding.root) {

        fun bind(item: MusicItem) {
            itemFavoriteBinding.item = item
            itemFavoriteBinding.executePendingBindings()
        }
    }
}