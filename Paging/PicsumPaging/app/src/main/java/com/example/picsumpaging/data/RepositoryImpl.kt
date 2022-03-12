package com.example.picsumpaging.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.picsumpaging.data.local.LocalImageDatabase
import com.example.picsumpaging.paging.ImagePagingRemoteMediator
import com.example.picsumpaging.paging.ImagePagingSource
import com.example.picsumpaging.paging.item.ImageData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class RepositoryImpl @Inject constructor(
    private val picsumApi: PicsumApi,
    private val database: LocalImageDatabase
) : Repository {
    override fun getImagePagingData(): Flow<PagingData<ImageData>> {
        val pagingSourceFactory = { database.getLocalImageDao().getAllImages() }
        return Pager(
            config = PagingConfig(pageSize = 8),
            remoteMediator = ImagePagingRemoteMediator(database, picsumApi),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun deleteImage(id: String) {
        database.getLocalImageDao().deleteImage(id)
    }
}