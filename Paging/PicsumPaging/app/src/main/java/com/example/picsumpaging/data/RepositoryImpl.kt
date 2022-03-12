package com.example.picsumpaging.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.example.picsumpaging.paging.ImagePagingSource
import com.example.picsumpaging.paging.item.ImageData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class RepositoryImpl @Inject constructor(
    private val picsumApi: PicsumApi
) : Repository {
    override fun getImagePagingData(): Flow<PagingData<ImageData>> {
        return Pager(PagingConfig(pageSize = 8)) {
            ImagePagingSource(picsumApi)
        }.flow
    }
}