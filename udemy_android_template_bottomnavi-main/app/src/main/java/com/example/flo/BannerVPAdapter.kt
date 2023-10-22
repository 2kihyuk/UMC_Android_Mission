package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment){

    private val fragmentlist : ArrayList<Fragment> = ArrayList() //여러 프래그먼트를 담아둘 공간.

    override fun getItemCount():Int= fragmentlist.size




    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addFragment(fragment:Fragment){  //함수가 처음 실행될떄 프래그먼트 리스트에는 아무것도 없는데, 홈프래그먼트에서 추가해줄 프래그먼트를 써주기위해 사용하는 함수.
        //
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1)
    }


}