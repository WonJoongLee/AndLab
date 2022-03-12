package com.example.picsumpaging.paging.item

import com.google.gson.annotations.SerializedName

// todo 확인 한 번 해보기
data class ImageData(
    val id: String,
    @SerializedName("download_url")
    val url: String
)
