package com.bottotop.local

import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val sampleDao: SampleDao
) : LocalDataSource {

    override suspend fun insertSample(sample: LocalEntity) {
        sampleDao.insert(sample)
    }

    override suspend fun getAllSamples() = sampleDao.getAllSamples()
}