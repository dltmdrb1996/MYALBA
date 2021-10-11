package com.bottotop.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SampleDao {

    @Insert
    suspend fun insert(obj: LocalEntity): Long

    @Query("SELECT * FROM sample")
    fun getAllSamples(): Flow<List<LocalEntity>>
}