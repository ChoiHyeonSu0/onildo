package com.likelion.onildo.data.repository

import com.likelion.onildo.data.local.TilDao
import com.likelion.onildo.data.local.TilEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TilRepository @Inject constructor(private val dao: TilDao) {

    suspend fun insertTil(til: TilEntity): Result<Long> =
        runCatching { dao.insertTil(til) }

    suspend fun getAllTils(): Result<Flow<List<TilEntity>>> =
        runCatching { dao.getAllTils() }

    suspend fun deleteTil(til: TilEntity): Result<Unit> =
        runCatching { dao.deleteTil(til) }
}