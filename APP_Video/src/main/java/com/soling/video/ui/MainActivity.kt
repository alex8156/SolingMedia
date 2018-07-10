package com.soling.video.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.soling.autosdk.usb.UsbConst
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.util.ViewFlags
import com.soling.media.util.mylog
import com.soling.media.util.transaction
import com.soling.media.util.videoAppChanged
import com.soling.media.viewmodel.UsbMediaViewModel
import com.soling.media.viewmodel.UsbMediaViewModelPool
import com.soling.media.viewmodel.VideoPlayItemViewModel
import com.soling.music.ui.UsbListFragment
import com.soling.music.ui.VideoPlayFragment
import com.soling.video.R
import kotlinx.android.synthetic.main.activity_video.*

class MainActivity : AppCompatActivity() {

    lateinit var listFragment: Fragment

    lateinit var playFragment : Fragment

    lateinit var usbMediaViewModel : UsbMediaViewModel

    lateinit var  videoPlayItemViewModel : VideoPlayItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mylog("VideoActivity onCreate")
        setContentView(R.layout.activity_video)
        listFragment = UsbListFragment()
        playFragment = VideoPlayFragment()
        transaction { add(R.id.fragment_container,listFragment)}
        transaction { add(R.id.fragment_container,playFragment) }
        showPlayFragment()
        usbMediaViewModel = ViewModelProviders.of(this, Injection.usbVideoViewModelFactory(MediaApplication.getInstance())).get(UsbMediaViewModel::class.java)
        videoPlayItemViewModel = ViewModelProviders.of(this,  Injection.videoPlayItemViewModelFactory(MediaApplication.getInstance())).get(VideoPlayItemViewModel::class.java);
        usbMediaViewModel.viewFlagLiveData.observe(this, Observer {
            it?.let {
                mylog("viewFlag==>USB_IN_FLAG=${it and ViewFlags.USB_IN_FLAG};LIST_NOT_EMPTY_FLAG=${it and ViewFlags.LIST_NOT_EMPTY_FLAG};PARK_ENABLE_FLAG=${it and ViewFlags.PARK_ENABLE_FLAG};SCAN_START_FLAG=${it and ViewFlags.SCAN_START_FLAG}")
                if(((it and ViewFlags.USB_IN_FLAG) != 0) && ((it and ViewFlags.SCAN_START_FLAG )!= 0)) {
                    scan_panel.visibility = View.VISIBLE
                    mylog("当前线程=》${Thread.currentThread().name}")
                    tv_scan_state.setText("扫描中...")
                    mylog("扫描中...")
                }  else {
                    scan_panel.visibility = View.GONE
                    mylog("扫描完成")
                }

                if(it and ViewFlags.USB_IN_FLAG != 0 &&  it and ViewFlags.SCAN_START_FLAG == 0 && it and  ViewFlags.LIST_NOT_EMPTY_FLAG != 0 &&  it and ViewFlags.PARK_ENABLE_FLAG == 0) {
                    fragment_container.visibility = View.VISIBLE

                } else  {
                    fragment_container.visibility = View.GONE
                }

                if(it and ViewFlags.USB_IN_FLAG != 0 && it and ViewFlags.SCAN_START_FLAG == 0 && it and  ViewFlags.LIST_NOT_EMPTY_FLAG == 0 ) {
                    scan_panel.visibility = View.VISIBLE
                    tv_scan_state.setText("无视频文件")
                }

                if(it and ViewFlags.USB_IN_FLAG != 0 && it and ViewFlags.SCAN_START_FLAG == 0 && it and ViewFlags.LIST_NOT_EMPTY_FLAG != 0 && it and ViewFlags.PARK_ENABLE_FLAG != 0)
                 tv_park.visibility = View.VISIBLE
                else tv_park.visibility = View.GONE

                if(it and ViewFlags.USB_IN_FLAG == 0) {
                    scan_panel.visibility = View.VISIBLE
                    tv_scan_state.setText("未检测到U盘")
//                    videoPlayItemViewModel.unmount()
                }

            }

        })
        usbMediaViewModel.mountFlagLiveData.observe(this, Observer {
            it?.let {
//                if(!it)finish()
            }
        })
    }

    fun showPlayFragment() {
        transaction { show(playFragment).hide(listFragment) }
    }

    fun showListFragment() {
        transaction { show(listFragment).hide(playFragment) }

    }

    override fun onResume() {
        super.onResume()
//        videoPlayItemViewModel.play_pause(true)
        videoAppChanged(true)
    }

    override fun onPause() {
//        videoPlayItemViewModel.play_pause(false)
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mylog("VideoActivity onDestroy")
        usbMediaViewModel.mediaListRepository.autoPlayLiveData.value = null
//        UsbMediaViewModelPool.getInstance().clear()
    }

    override fun onBackPressed() {
        if(listFragment.isHidden)showListFragment() else super.onBackPressed()
    }

}