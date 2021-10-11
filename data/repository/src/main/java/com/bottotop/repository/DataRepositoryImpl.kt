package com.bottotop.repository

import android.util.Log
import com.bottotop.model.Sample
import com.bottotop.local.LocalDataSource
import com.bottotop.model.repository.DataRepository
import com.bottotop.remote.ApiService
import com.bottotop.repository.mapper.SampleEntityMapper
import javax.inject.Inject

internal class DataRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val apiService: ApiService,
) : DataRepository {

    override suspend fun getUser(id : String , test : String): Sample {
        Log.e("sdf", "getUser: ${apiService.getUser(id,test)}", )
        return SampleEntityMapper.from(apiService.getUser(id,test))
    }
}