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
import com.bottotop.repository.mapper.UserLocalEntityMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import javax.inject.Inject

internal class DataRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val apiService: ApiService,
) : DataRepository {



    override suspend fun getUser(): User {
        return UserLocalEntityMapper.from(localDataSource.getUser())
    }

    override suspend fun setUser(info: Map<String, String>) {
        val json = Json.encodeToString(info)
        apiService.setUser(json)
        info["id"]?.let { refreshUser(it) }
    }

    override suspend fun refreshUser(id : String) {
        val user = UserEntityMapper.from(apiService.getUser(id))
        val entity = UserLocalEntityMapper.to(user)
        localDataSource.insertUser(entity)

    }


    override suspend fun refreshAndGetUser(id: String): User {
        refreshUser(id)
        return getUser()
    }

    override suspend fun updateUser(query : Map<String,String>) {
        val json = Json.encodeToString(query)
        apiService.updateUser(json)
    }

    override suspend fun getCompanies(id: String): List<Company> {
        val result = apiService.getCompanies(id)
        return result.companies.map {
            CompanyMapper.from(it)
        }
    }


    override suspend fun setCompany(info : Map<String,String>){
        val json = Json.encodeToString(info)
        apiService.setCompany(json)
    }
}