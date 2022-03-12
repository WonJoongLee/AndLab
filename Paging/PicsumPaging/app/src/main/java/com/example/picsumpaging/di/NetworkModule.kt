package com.example.picsumpaging.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.picsumpaging.data.PicsumApi
import com.example.picsumpaging.data.Repository
import com.example.picsumpaging.data.RepositoryImpl
import com.example.picsumpaging.data.local.LocalImageDatabase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val PICSUM_BASE_URL = "https://picsum.photos/"

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun provideRetrofit() =
        Retrofit.Builder()
            .baseUrl(PICSUM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun providePicsumAPI(): PicsumApi = provideRetrofit().create(PicsumApi::class.java)

    @ExperimentalPagingApi
    @Provides
    fun bindRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl
}