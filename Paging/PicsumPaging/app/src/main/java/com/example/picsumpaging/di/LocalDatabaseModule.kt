package com.example.picsumpaging.di

import android.content.Context
import androidx.room.Room
import com.example.picsumpaging.data.local.LocalImageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Contextual
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): LocalImageDatabase {
        return Room.databaseBuilder(
            appContext,
            LocalImageDatabase::class.java,
            "localImage.db"
        ).build()
    }
}