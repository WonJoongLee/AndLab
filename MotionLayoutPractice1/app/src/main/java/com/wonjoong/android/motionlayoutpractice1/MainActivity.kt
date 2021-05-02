package com.wonjoong.android.motionlayoutpractice1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wonjoong.android.motionlayoutpractice1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btYoutubeAndroidDeveloper.setOnClickListener {
            val intent = Intent(this, YoutubeActivity::class.java)
            startActivity(intent)
        }

        binding.btYoutubeCodeWithJoyce.setOnClickListener {
            val intent = Intent(this, JoyceActivity::class.java)
            startActivity(intent)
        }
    }
}