package com.soling.music.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.music.R
import com.soling.music.databinding.FragmentMusicPlayBinding
import com.soling.media.viewmodel.NewPlayItemViewModel

class MusicPlayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentMusicPlayFragment = DataBindingUtil.inflate<FragmentMusicPlayBinding>(inflater, R.layout.fragment_music_play,container,false)
        fragmentMusicPlayFragment.viewmodel = ViewModelProviders.of(activity!!,  Injection.musicPlayItemViewModelFactory(MediaApplication.getInstance())).get(NewPlayItemViewModel::class.java);
        fragmentMusicPlayFragment.navigateionHandler = NavigateionHandler()
        fragmentMusicPlayFragment.setLifecycleOwner(this)
        return fragmentMusicPlayFragment.root
    }

    inner class NavigateionHandler {
        fun navigateListFragment() {
            (activity as MainActivity).showListFragment()
        }
    }

}