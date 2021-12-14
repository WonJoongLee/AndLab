package com.wonjoong.sandbox.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.wonjoong.sandbox.R
import com.wonjoong.sandbox.databinding.FragmentMainBinding
import com.wonjoong.sandbox.util.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCamera.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCameraFragment())
        }
        binding.btnCustomView.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToCustomViewFragment())
        }
    }
}