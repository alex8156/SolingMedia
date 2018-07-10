package com.soling.music.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.persistence.model.MediaItem
import com.soling.media.persistence.model.VideoItem
import com.soling.media.util.mylog
import com.soling.media.viewmodel.*
import com.soling.video.R
import com.soling.video.ui.MainActivity
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import com.soling.video.databinding.*
import kotlinx.coroutines.experimental.launch

class UsbListFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentListBinding = DataBindingUtil.inflate<FragmentListBinding>(inflater, R.layout.fragment_list,container,false)
        val playViewmodel = ViewModelProviders.of(activity!!, Injection.videoPlayItemViewModelFactory(MediaApplication.getInstance())).get(VideoPlayItemViewModel::class.java)
        val usbMediaViewModel = ViewModelProviders.of(activity!!, Injection.usbVideoViewModelFactory(MediaApplication.getInstance())).get(UsbMediaViewModel::class.java)
        val adpater = VideoListAdapter(activity?.applicationContext, { holder, list ->
            holder?.itemVideoBinding?.root?.setOnClickListener({
                (activity as MainActivity).showPlayFragment()
                playViewmodel.playItem(holder.itemVideoBinding.item!!)
            })
        })
        fragmentListBinding.recycelView.apply {
            layoutManager = GridLayoutManager(activity?.applicationContext,4)
            adapter = adpater

        }
//        MediaApplication.getInstance().musicListViewModel.usbLiveData.observe(
//                this, Observer<List<MusicItem>> { adpater.replaceData(it) })
        usbMediaViewModel.usbLiveData.observe(this, Observer {
                val list = it as List<VideoItem>
                adpater.replaceData(list)
                mylog("切换播放列表")
                if(list.isNotEmpty()) {
                    playViewmodel.replaceList(list)
                }

           })
        usbMediaViewModel.autoPlayLiveData.observe(this, Observer {
            it?.let {
                it as VideoItem
                mylog("自动播放:$it")
                playViewmodel.playItem(it)
                (activity as MainActivity).showPlayFragment()
            }
        })
//        val typeItemm = Type<ItemMusicBinding>(R.layout.item_music)
//                .onBind { }
//                .onClick {
//                    val usbMediaViewModels = MediaApplication.getInstance().musicListViewModel.usbLiveData.value
//                    if (usbMediaViewModels != null)
//                        it.binding.item?.also {
//                            playViewmodel.onListChanged(usbMediaViewModels, it)
//                        }
//                }
//       val a = LastAdapter(MediaApplication.getInstance().musicListViewModel.usbLiveData.value!!, BR.item).type { item, position ->
//            typeItemm
//        }.layout { item, position ->
//            R.layout.item_music
//        }.into(recycel_view)
        return fragmentListBinding.root
    }


}