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
    private lateinit var pagingAdapter: ImagePagingAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        collectImage()

        // network 네트워크 연결이 되지 않았을 때도 app inspection에서 디비를 보고싶으면
        // 아래 주석을 해제하면 된다.
        // openOrCreateDatabase("localImage.db", MODE_PRIVATE, null)
    }

    private fun initAdapter() {
        pagingAdapter = ImagePagingAdapter(viewModel::deleteImage)
        binding.rvMain.adapter = pagingAdapter
    }

    private fun collectImage() {
        lifecycleScope.launchWhenStarted {
            viewModel.imageDataFlow.collect { newImageDataList ->
                pagingAdapter.submitData(newImageDataList)
            }
        }
    }


}