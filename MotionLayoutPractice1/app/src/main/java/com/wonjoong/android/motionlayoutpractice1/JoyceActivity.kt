package com.wonjoong.android.motionlayoutpractice1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wonjoong.android.motionlayoutpractice1.databinding.ActivityYoutubeJoyceBinding

class JoyceActivity : AppCompatActivity(){
    private lateinit var binding : ActivityYoutubeJoyceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_joyce)


    }
}