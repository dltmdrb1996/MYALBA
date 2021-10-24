package com.bottotop.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.bottotop.local.LocalDataSource
import com.bottotop.model.Company
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import com.bottotop.remote.ApiService
import com.bottotop.repository.mapper.CompanyMapper
import com.bottotop.repository.mapper.UserEntityMapper
import com.bottotop.repository.mapper.UserLocalEntityMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    override suspend fun refreshUser(id : String) : Boolean {
        val response = apiService.getUser(id)
        if(response.code()==200){
            val user = UserEntityMapper.from(response.body()!!)
            val entity = UserLocalEntityMapper.to(user)
            localDataSource.insertUser(entity)
            Log.e(TAG, "refreshUser: 유저정보 불러오기 성공", )
            return true
        }else if(response.code()==404){
            Log.e(TAG, "refreshUser: 찾는 유저정보가 없음", )
            return false
        }else{
            Log.e(TAG, "refreshUser: 인터넷&서버에러", )
            return false
        }
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
        val response = apiService.getCompanies(id).execute()
        Log.e("getCompanies", "getCompanies: ${response.code()}", )
        val result = response.body()!!
        return result.companies.map {
            CompanyMapper.from(it)
        }
    }


    override suspend fun setCompany(info : Map<String,String>){
        val json = Json.encodeToString(info)
        apiService.setCompany(json)
    }

    companion object{
        val TAG = "DataImpl"
    }
}