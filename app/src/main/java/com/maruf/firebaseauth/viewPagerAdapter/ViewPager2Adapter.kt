package com.maruf.firebaseauth.viewPagerAdapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList = mutableListOf<Fragment>()
    private val fragmentTitleList = mutableListOf<String>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    fun addFragment(fragment: Fragment, bundle: Bundle?) {
        fragment.arguments = bundle
        fragmentList.add(fragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}