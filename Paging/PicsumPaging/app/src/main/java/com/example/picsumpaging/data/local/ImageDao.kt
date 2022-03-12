package com.example.picsumpaging.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picsumpaging.paging.item.ImageData

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageData>)

//    @Query("SELECT * FROM images WHERE url LIKE :query")
//    fun pagingSource(query: String): PagingSource<Int, ImageData>

    @Query("DELETE FROM images")
    suspend fun clearAll()

    @Query("DELETE FROM images WHERE id=:id")
    fun deleteImage(id: String)

    @Query("SELECT * FROM images")
    fun getAllImages(): PagingSource<Int, ImageData>
}