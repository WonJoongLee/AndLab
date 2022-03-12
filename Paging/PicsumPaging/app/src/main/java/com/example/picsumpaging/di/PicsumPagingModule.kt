package com.example.picsumpaging.di

import com.example.picsumpaging.data.Repository
import com.example.picsumpaging.data.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object PicsumPagingModule {

    @Provides
    fun bindRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl
}
