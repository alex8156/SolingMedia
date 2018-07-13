package com.soling.music.ui

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.util.mylog
import com.soling.media.viewmodel.VideoPlayItemViewModel
import com.soling.video.R
import com.soling.video.databinding.*

class VideoPlayFragment : Fragment() {
    lateinit var  videoPlayItemViewModel:VideoPlayItemViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentVideolayFragment = DataBindingUtil.inflate<FragmentVideoPlayBinding>(inflater, R.layout.fragment_video_play,container,false)
        videoPlayItemViewModel = ViewModelProviders.of(activity!!,  Injection.videoPlayItemViewModelFactory(MediaApplication.getInstance())).get(VideoPlayItemViewModel::class.java);
        fragmentVideolayFragment.viewmodel = videoPlayItemViewModel
        fragmentVideolayFragment.setLifecycleOwner(this)
        fragmentVideolayFragment.surfaceView.holder.addCallback(object  : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                mylog("surfaceChanged,width=$width;height=$height;format=$format")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mylog("surfaceDestroyed")
                videoPlayItemViewModel.surfaceDestroyed()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                mylog("surfaceCreated")
                videoPlayItemViewModel.surfaceCreated(holder)
            }

        });

//        videoPlayItemViewModel.videoRadio.observe(this, Observer {
//            it?.let {
//                val layoutParams = fragmentVideolayFragment.surfaceView.layoutParams
//                layoutParams.width =
//                        if(videoPlayItemViewModel.screenRadio > videoPlayItemViewModel.videoRadio.value!!)
//                            (videoPlayItemViewModel.screenHeight * videoPlayItemViewModel.videoRadio.value!!).toInt()
//                        else
//                            videoPlayItemViewModel.screenHeight
//                layoutParams.height =
//                        if(videoPlayItemViewModel.screenRadio > videoPlayItemViewModel.videoRadio.value!!)
//                            videoPlayItemViewModel.screenHeight
//                        else
//                            (videoPlayItemViewModel.screenWidth/videoPlayItemViewModel.videoRadio.value!!).toInt()
//            }
//
//        })

        return fragmentVideolayFragment.root
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mylog("onHiddenChanged=${hidden}")
//        if(hidden)videoPlayItemViewModel.play_pause(false)
    }

}