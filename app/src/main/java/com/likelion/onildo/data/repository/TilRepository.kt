package com.likelion.onildo.data.repository

import com.likelion.onildo.data.local.TilDao
import com.likelion.onildo.data.local.TilEntity
import com.likelion.onildo.data.remote.TilRemoteDataSource
import com.likelion.onildo.model.TilAnalysisResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TilRepository @Inject constructor(
    private val dao: TilDao,
    private val dataSource: TilRemoteDataSource
    ) {

    suspend fun insertTil(til: TilEntity): Result<Long> =
        runCatching { dao.insertTil(til) }

    fun getAllTils(): Result<Flow<List<TilEntity>>> =
         runCatching { dao.getAllTils() }

    suspend fun getTilById(id: Long): Result<TilEntity?> =
        runCatching { dao.getTilById(id)  }

    suspend fun updateTil(til: TilEntity): Result<Unit> =
        runCatching { dao.updateTil(til) }

    suspend fun deleteTil(til: TilEntity): Result<Unit> =
        runCatching { dao.deleteTil(til) }

    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): Result<TilAnalysisResult> =
        runCatching { dataSource.analyzeTil(title, learned, difficulty, tomorrow) }
}