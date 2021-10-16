package com.bottotop.local

import com.bottotop.local.entity.LocalUserEntity

interface LocalDataSource {
    suspend fun insertSample(sample: LocalUserEntity)
    suspend fun getAllSamples(): LocalUserEntity
}