package com.example.picsumpaging

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.picsumpaging.databinding.ActivityMainBinding
import com.example.picsumpaging.paging.ImagePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val adapter = ImagePagingAdapter()
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMain.adapter = adapter
        collectImage()
    }

    private fun collectImage() {
        lifecycleScope.launchWhenStarted {
            viewModel.imageDataFlow.collect { newImageDataList ->
                adapter.submitData(newImageDataList)
            }
        }
    }
}