package com.likelion.onildo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TilDao {
    @Query("SELECT * FROM tils ORDER BY createdAt DESC")
    fun getAllTils(): Flow<List<TilEntity>>

    @Query("SELECT * FROM tils WHERE id = :id")
    suspend fun getTilById(id: Long): TilEntity?

    @Query("SELECT * FROM tils WHERE createdAt >= :startTime AND createdAt < :endTime ORDER BY createdAt DESC")
    fun getTilsByDateRange(startTime: Long, endTime: Long): Flow<List<TilEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTil(til: TilEntity): Long

    @Update
    suspend fun updateTil(til: TilEntity)

    @Delete
    suspend fun deleteTil(til: TilEntity)

    @Query("SELECT * FROM tils WHERE createdAt >= :startTime ORDER BY createdAt ASC")
    fun getTilsForMonth(startTime: Long): Flow<List<TilEntity>>
}