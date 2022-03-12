package com.example.picsumpaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.picsumpaging.data.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val imageDataFlow = repository.getImagePagingData()

    fun deleteImage(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteImage(id)
        }
    }
}
