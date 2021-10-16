package com.bottotop.repository

import android.util.Log
import com.bottotop.model.Sample
import com.bottotop.local.LocalDataSource
import com.bottotop.model.Company
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import com.bottotop.remote.ApiService
import com.bottotop.repository.mapper.CompanyMapper
import com.bottotop.repository.mapper.SampleEntityMapper
import com.bottotop.repository.mapper.UserEntityMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import javax.inject.Inject

internal class DataRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val apiService: ApiService,
) : DataRepository {

    override suspend fun getUser(id: String): User {
        return UserEntityMapper.from(apiService.getUser(id))
    }

    override suspend fun setUser(info: Map<String, String>) {
        val json = Json.encodeToString(info)
        apiService.setUser(json)
    }

    override suspend fun updateUser(query : Map<String,String>) {
        val json = Json.encodeToString(query)
        apiService.updateUser(json)
    }



    override suspend fun getCompanies(id: String): List<Company> {
        return apiService.getCompanies(id).companies.map {
            CompanyMapper.from(it)
        }
    }
}