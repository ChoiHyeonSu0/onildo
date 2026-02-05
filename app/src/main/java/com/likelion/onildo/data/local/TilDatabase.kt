package com.likelion.onildo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TilEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TilDatabase : RoomDatabase() {
    abstract fun tilDao(): TilDao

    companion object {
        @Volatile
        private var INSTANCE: TilDatabase? = null

        fun getDatabase(context: Context): TilDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TilDatabase::class.java,
                    "til_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}