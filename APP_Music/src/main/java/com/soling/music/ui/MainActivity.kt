package com.soling.music.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.soling.media.Injection
import com.soling.media.MediaApplication
import com.soling.media.persistence.model.MusicItem
import com.soling.media.util.ViewFlags
import com.soling.media.util.musicAppChanged
import com.soling.media.util.mylog
import com.soling.media.viewmodel.UsbMediaViewModelPool
import com.soling.music.R
import com.soling.media.util.transaction
import com.soling.media.viewmodel.UsbMediaViewModel
import kotlinx.android.synthetic.main.activity_music.*

/**
 * Created by yizeng on 2018/5/7.
 */
class MainActivity : AppCompatActivity() {


    lateinit var musicListFragment: Fragment

    lateinit var musicPlayFragment : Fragment

    lateinit var usbMediaViewModel : UsbMediaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        musicListFragment = MusicListFragment()
        musicPlayFragment = MusicPlayFragment()
        transaction { add(R.id.fragment_container,musicListFragment)}
        transaction { add(R.id.fragment_container,musicPlayFragment) }
        showPlayFragment()

        usbMediaViewModel = ViewModelProviders.of(this, Injection.usbMusicViewModelFactory(MediaApplication.getInstance())).get(UsbMediaViewModel::class.java)
        usbMediaViewModel.viewFlagLiveData.observe(this, Observer {
            it?.let {
                mylog("viewFlag==>USB_IN_FLAG=${it and ViewFlags.USB_IN_FLAG};LIST_NOT_EMPTY_FLAG=${it and ViewFlags.LIST_NOT_EMPTY_FLAG};PARK_ENABLE_FLAG=${it and ViewFlags.PARK_ENABLE_FLAG};SCAN_START_FLAG=${it and ViewFlags.SCAN_START_FLAG}")
                if(it and ViewFlags.USB_IN_FLAG == 0 ) {
                    scan_panel.visibility = View.VISIBLE
                    tv_scan_state.setText("未检测到U盘")
//                    videoPlayItemViewModel.unmount()
                }
                if( ((it and ViewFlags.SCAN_START_FLAG )!= 0)) {
                    scan_panel.visibility = View.VISIBLE
                    tv_scan_state.setText("正在加载中...")
                    mylog("正在加载中...")
                }

                if(it and ViewFlags.SCAN_START_FLAG == 0 && it and ViewFlags.USB_IN_FLAG != 0 &&  it and  ViewFlags.LIST_NOT_EMPTY_FLAG != 0) {
                    scan_panel.visibility = View.GONE
                }

                if(it and ViewFlags.USB_IN_FLAG != 0 &&  it and ViewFlags.SCAN_START_FLAG == 0 && it and  ViewFlags.LIST_NOT_EMPTY_FLAG != 0 &&  it and ViewFlags.PARK_ENABLE_FLAG == 0) {
                    fragment_container.visibility = View.VISIBLE

                } else  {
                    fragment_container.visibility = View.GONE
                }

                if(it and ViewFlags.USB_IN_FLAG != 0 && it and ViewFlags.SCAN_START_FLAG == 0 && it and  ViewFlags.LIST_NOT_EMPTY_FLAG == 0 ) {
                    scan_panel.visibility = View.VISIBLE
                    tv_scan_state.setText("无音频文件")
                }

                if(it and ViewFlags.USB_IN_FLAG != 0 && it and ViewFlags.SCAN_START_FLAG == 0 && it and ViewFlags.LIST_NOT_EMPTY_FLAG != 0 && it and ViewFlags.PARK_ENABLE_FLAG != 0)
                    tv_park.visibility = View.VISIBLE
                else tv_park.visibility = View.GONE
            }

        })

    }

    fun showPlayFragment() {
        transaction { show(musicPlayFragment).hide(musicListFragment) }
    }

    fun showListFragment() {
        transaction { show(musicListFragment).hide(musicPlayFragment) }
    }

    override fun onResume() {
        super.onResume()
        musicAppChanged(true)
    }

    override fun onStop() {
        super.onStop()
        musicAppChanged(false)
    }

    override fun onDestroy() {
        super.onDestroy()
//        UsbMediaViewModelPool.getInstance().clear()
    }

    override fun onBackPressed() {
        if(musicListFragment.isHidden)showListFragment() else super.onBackPressed()
    }

}