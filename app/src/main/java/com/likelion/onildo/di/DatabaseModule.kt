package com.likelion.onildo.di

import android.content.Context
import androidx.room.Room
import com.likelion.onildo.data.local.TilDao
import com.likelion.onildo.data.local.TilDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): TilDatabase {
        return Room.databaseBuilder(
            context,
            TilDatabase::class.java,
            "onildo_db"
        ).build()
    }

    @Provides
    fun provideTilDao(database: TilDatabase): TilDao {
        return database.tilDao() // OnildoDatabase에 정의된 추상 함수 호출
    }
}