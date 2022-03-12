package com.example.picsumpaging.paging.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// todo 확인 한 번 해보기
@Entity(tableName = "images")
data class ImageData(
    @PrimaryKey val id: String,
    @SerializedName("download_url")
    val url: String
)
