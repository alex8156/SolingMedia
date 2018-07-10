package com.soling.music.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soling.music.R
import com.soling.music.databinding.FragmentMusicListBinding
import com.soling.media.util.transaction

class MusicListFragment : Fragment() {

    lateinit var favoriteFragment: FavoriteFragment

    lateinit var internalFragment: InternalFragment

    lateinit var usbFragment: UsbFragment


    val hindAll = { transaction : FragmentTransaction -> transaction.hide(favoriteFragment).hide(internalFragment)
            .hide(usbFragment)
        transaction
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentMusicListBinding = DataBindingUtil.inflate<FragmentMusicListBinding>(inflater, R.layout.fragment_music_list,container,false)
        fragmentMusicListBinding.checkHandler = CheckHandler()
        favoriteFragment = FavoriteFragment()

        internalFragment = InternalFragment()

        usbFragment = UsbFragment()

        transaction { add(R.id.fl_childFragment,favoriteFragment)
                .add(R.id.fl_childFragment,internalFragment)
                .add(R.id.fl_childFragment,usbFragment)
            hindAll(this).show(usbFragment)
        }



        return fragmentMusicListBinding.root
    }

    inner class CheckHandler{

        fun checkedUsb(view : View,value:Boolean) {
            if (value) {
                transaction {
                    hindAll(this).show(usbFragment)
                }
            }
        }


        fun checkedFavorite(view : View,value:Boolean) {
            if (value) {
                transaction {
                    hindAll(this).show(favoriteFragment)
                }
            }
        }

        fun checkedInternal(view : View,value:Boolean) {
            if (value) {
                transaction {
                    hindAll(this).show(internalFragment)
                }
            }
        }


    }

}