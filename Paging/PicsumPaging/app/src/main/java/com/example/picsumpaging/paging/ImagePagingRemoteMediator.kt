package com.example.picsumpaging.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.picsumpaging.data.PicsumApi
import com.example.picsumpaging.data.local.LocalImageDatabase
import com.example.picsumpaging.data.local.RemoteKey
import com.example.picsumpaging.paging.item.ImageData

@ExperimentalPagingApi
class ImagePagingRemoteMediator(
    private val localDatabase: LocalImageDatabase,
    private val api: PicsumApi
) : RemoteMediator<Int, ImageData>() {
    private val imagesDao = localDatabase.getLocalImageDao()
    private val keysDao = localDatabase.getKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageData>
    ): MediatorResult {
        return try {
            val page: Int = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getFirstRemoteKey(state)
                    val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getLastRemoteKey(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextKey
                }
            }

            val response = api.getImages(page, state.config.pageSize)
            val isEndOfList = response.isNullOrEmpty()

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (isEndOfList) null else page + 1
            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    imagesDao.clearAll()
                    keysDao.clearAll()
                }

                val keys = response.map {
                    RemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keysDao.insertAll(remoteKey = keys)
                imagesDao.insertAll(images = response)
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ImageData>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { image ->
                keysDao.remoteKeysImageId(image)
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, ImageData>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { image -> keysDao.remoteKeysImageId(image.id) }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, ImageData>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { image -> keysDao.remoteKeysImageId(image.id) }
    }
}