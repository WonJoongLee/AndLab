package com.wonjoong.sandbox.customview

import android.os.Bundle
import android.util.Log
import android.view.View
import com.wonjoong.sandbox.R
import com.wonjoong.sandbox.databinding.FragmentCustomViewBinding
import com.wonjoong.sandbox.util.BaseFragment

class CustomViewFragment : BaseFragment<FragmentCustomViewBinding>(R.layout.fragment_custom_view) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.civ.setOnClickListener {
            Log.e("clicked", "clicked!")
        }
    }
}