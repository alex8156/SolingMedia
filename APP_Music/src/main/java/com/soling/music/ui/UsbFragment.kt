package com.soling.music.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.music.R
import com.soling.music.databinding.ChildFragmentListBinding
import com.soling.media.persistence.model.MusicItem
import com.soling.media.util.mylog
import com.soling.media.viewmodel.*
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.io.File

class UsbFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val childFragmentFavoritelistBinding = DataBindingUtil.inflate<ChildFragmentListBinding>(inflater, R.layout.child_fragment_list,container,false)
        val playViewmodel = ViewModelProviders.of(activity!!, Injection.musicPlayItemViewModelFactory(MediaApplication.getInstance())).get(NewPlayItemViewModel::class.java)
        val usbMediaViewModel = ViewModelProviders.of(activity!!, Injection.usbMusicViewModelFactory(MediaApplication.getInstance())).get(UsbMediaViewModel::class.java)
        val adpater = MusicListAdapter(activity?.applicationContext, { holder, list ->
            holder?.itemMusicBinding?.root?.setOnClickListener({
                (activity as MainActivity).showPlayFragment()
                playViewmodel.playItem(holder.itemMusicBinding.item!!)
            })
            holder?.itemMusicBinding?.btDelete?.setOnClickListener({
                holder?.itemMusicBinding?.item?.also { musicItem ->
                    usbMediaViewModel.deleteItem(musicItem)
                    launch {
                        musicItem.path.let {
                        val deleted  = File(musicItem.path).delete()
                        mylog("file deleted==>$deleted")
                         }
                    }
                }
            })
            holder?.itemMusicBinding?.tbLike?.setOnClickListener({ view
                holder?.itemMusicBinding?.item?.also { musicItem ->
                    musicItem.isFavorite = !musicItem.isFavorite
                    usbMediaViewModel.updateItem(musicItem)
                }
            })
        })
        childFragmentFavoritelistBinding.recycelView.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = adpater
        }
//        MediaApplication.getInstance().musicListViewModel.usbLiveData.observe(
//                this, Observer<List<MusicItem>> { adpater.replaceData(it) })
        usbMediaViewModel.usbLiveData.observe(this, Observer {
                val list = it as List<MusicItem>
                adpater.replaceData(list)
                mylog("切换播放列表")
                if(list.isNotEmpty()) {
                    playViewmodel.replaceList(list)
                }
//                musicItem?:list.firstOrNull()?.also {  playViewmodel.replaceList(list,it)}
           })
        usbMediaViewModel.autoPlayLiveData.observe(this, Observer {
            it?.let {
                it as MusicItem
                mylog("自动播放:$it")
                playViewmodel.playItem(it)
                (activity as MainActivity).showPlayFragment()
            }
        })
        childFragmentFavoritelistBinding.tvFlag.setText("USB")
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
        return childFragmentFavoritelistBinding.root
    }


}