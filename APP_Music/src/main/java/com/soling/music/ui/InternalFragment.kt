package com.soling.music.ui

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
import com.soling.media.util.mylog
import com.soling.music.R
import com.soling.music.databinding.ChildFragmentInternalListBinding
import com.soling.media.viewmodel.InternalMusicListViewModel
import com.soling.media.viewmodel.NewPlayItemViewModel
import kotlinx.coroutines.experimental.launch
import java.io.File

class InternalFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val childFragmentInternalListBinding = DataBindingUtil.inflate<ChildFragmentInternalListBinding>(inflater, R.layout.child_fragment_internal_list,container,false)

        val playViewmodel = ViewModelProviders.of(activity!!, Injection.musicPlayItemViewModelFactory(MediaApplication.getInstance())).get(NewPlayItemViewModel::class.java)
        val internalMusicListViewModel = ViewModelProviders.of(activity!!,Injection.internalMusicViewModelFactory(MediaApplication.getInstance())).get(InternalMusicListViewModel::class.java)
        childFragmentInternalListBinding.tvFlag.setText("本地")

        val adpater = MusicListAdapter(activity?.applicationContext, { holder, list ->
            holder?.itemMusicBinding?.root?.setOnClickListener({
                (activity as MainActivity).showPlayFragment()
                playViewmodel.playItem(holder.itemMusicBinding.item!!)
            })
            holder?.itemMusicBinding?.btDelete?.setOnClickListener({
                holder?.itemMusicBinding?.item?.also { musicItem ->
                    internalMusicListViewModel.deleteItem(musicItem)
                    launch {
                        musicItem.path.let {
                            val deleted  = File(musicItem.path).delete()
                            mylog("file deleted==>$deleted")
                        }
                    }
                 }
            })

            holder?.itemMusicBinding?.tbLike?.setOnClickListener({ view ->
                holder?.itemMusicBinding?.item?.also { musicItem ->
                musicItem.isFavorite = !musicItem.isFavorite
                internalMusicListViewModel.updateItem(musicItem)
                }
            })

        })

       childFragmentInternalListBinding.recycelView?.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            adapter = adpater
        }

//        recycel_view.layoutManager = LinearLayoutManager(activity?.applicationContext)
//
//        val typeItemm = Type<ItemMusicBinding>(R.layout.item_music)
//                .onBind { }
//                .onClick {
//                    val list = intenalListViewModel.intenalLiveData.value
//                    if (list != null)
//                        it.binding.item?.also {
//                            playViewmodel.onListChanged(list, it)
//                        }
//                }
//
//
//        LastAdapter(intenalListViewModel.intenalLiveData.value!!, BR.item).type { item, position ->
//            typeItemm
//        }.layout { item, position ->
//            R.layout.item_music
//        }.into(recycel_view)
        return childFragmentInternalListBinding.root
    }
}