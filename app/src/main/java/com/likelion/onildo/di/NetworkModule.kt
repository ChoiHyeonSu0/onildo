package com.likelion.onildo.di

import com.likelion.onildo.BuildConfig
import com.likelion.onildo.data.remote.OpenAIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOpenAIService(): OpenAIService {
        // 실제로는 BuildConfig나 환경 변수에서 가져오는 것을 권장합니다.
        val apiKey = BuildConfig.OPEN_API_KEY
        return OpenAIService(apiKey)
    }
}