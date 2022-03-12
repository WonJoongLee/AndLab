package com.example.picsumpaging.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.picsumpaging.data.PicsumApi
import com.example.picsumpaging.paging.item.ImageData
import javax.inject.Inject

class ImagePagingSource @Inject constructor(
    private val pcisumApi: PicsumApi
) : PagingSource<Int, ImageData>() {

    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        return try {
            val page = params.key ?: 1
            val response = pcisumApi.getImages(page = page, limit = params.loadSize)

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page - 1, // if (page == 1) null else page - 1,
                nextKey = if (response.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
