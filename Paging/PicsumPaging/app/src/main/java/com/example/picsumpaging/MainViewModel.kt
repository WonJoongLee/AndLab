package com.example.picsumpaging

import androidx.lifecycle.ViewModel
import com.example.picsumpaging.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    val imageDataFlow = repository.getImagePagingData()
}