package com.example.picsumpaging.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.picsumpaging.paging.item.ImageData

@Database(entities = [ImageData::class, RemoteKey::class], version = 1)
abstract class LocalImageDatabase: RoomDatabase() {
    abstract fun getLocalImageDao(): ImageDao
    abstract fun getKeysDao(): RemoteKeyDao
}