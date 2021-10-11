package com.bottotop.local

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertSample(sample: LocalEntity)
    suspend fun getAllSamples(): Flow<List<LocalEntity>>
}