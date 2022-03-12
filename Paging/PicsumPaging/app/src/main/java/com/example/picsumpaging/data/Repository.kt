package com.example.picsumpaging.data

import androidx.paging.PagingData
import com.example.picsumpaging.paging.item.ImageData
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getImagePagingData(): Flow<PagingData<ImageData>>
    suspend fun deleteImage(id: String)
}