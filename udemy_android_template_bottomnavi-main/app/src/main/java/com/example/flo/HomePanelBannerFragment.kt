package com.example.flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentHomePanelBannerBinding


class HomePanelBannerFragment(val imgRes : Int): Fragment() {
    lateinit var binding: FragmentHomePanelBannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePanelBannerBinding.inflate(inflater, container, false)
        binding.homePanelImageIv.setImageResource(imgRes)

        return binding.root
    }
}