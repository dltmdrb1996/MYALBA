package com.bottotop.repository

import android.util.Log
import com.bottotop.local.LocalDataSource
import com.bottotop.model.*
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.wrapper.APIResult
import com.bottotop.model.wrapper.Result
import com.bottotop.remote.ApiService
import com.bottotop.repository.mapper.*
import com.bottotop.repository.mapper.CompanyEntityMapper
import com.bottotop.repository.mapper.CompanyMapper
import com.bottotop.repository.mapper.ScheduleMapper
import com.bottotop.repository.mapper.UserEntityMapper
import com.bottotop.repository.mapper.UserMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal class DataRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val apiService: ApiService,
) : DataRepository {

    // REFRESH //
    ////////////////////////////////////////////////////////////////
    override suspend fun refreshUser(id: String): APIResult {
        return try {
            val response = apiService.getUser(id)
            when {
                response.code() == 200 -> {
                    if (response.body() == null) return APIResult.Error(APIError.NullValueError)
                    val entity = UserEntityMapper.from(response.body()!!)
                    localDataSource.insertUser(entity)
                    APIResult.Success
                }
                else -> handleError(response.code(), "refreshUser")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "refreshUser : ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun refreshCompanies(id: String): APIResult {
        return try {
            val response = apiService.getCompanies(id)
            when {
                response.code() == 200 -> {
                    if (response.body() == null) return APIResult.Error(APIError.NullValueError)
                    response.body()!!.companies.forEach {
                        val company = CompanyEntityMapper.from(it)
                        localDataSource.insertCompany(company)
                        val responseUser = apiService.getUser(it.PK)
                        if (responseUser.code() == 200) {
                            val user = UserEntityMapper.from(responseUser.body()!!)
                            localDataSource.insertUser(user)
                        } else {
                            Log.e(TAG, "refreshCompanies: 맴버저장과정에서 에러발생")
                        }
                    }
                    APIResult.Success
                }
                else -> handleError(response.code(), "refreshCompanies")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "refreshCompanies : ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

//    override suspend fun refreshMember(): APIResult {
//
//    }


    // GET //
    ////////////////////////////////////////////////////////////////
    override suspend fun getUser(id: String): User {
        return try {
            val user = UserMapper.from(localDataSource.getUser(id))
            user
        } catch (e: Exception) {
            Log.e(TAG, "getUser: Room에서 유저불러오기 에러")
            error(e)
        }
    }

    override suspend fun getMembers(): List<User> {
        return try {
            val memberEntity = localDataSource.getMember()
            val member = memberEntity.map {
                UserMapper.from(it)
            }
            member
        } catch (e: Exception) {
            Log.e(TAG, "getUser: Room에서 유저불러오기 에러")
            error(e)
        }
    }

    override suspend fun getCompany(id: String): Company {
        return try {
            val memberEntity = localDataSource.getCompany(id)
            val member = CompanyMapper.from(memberEntity)
            member
        } catch (e: Exception) {
            Log.e(TAG, "getUser: Room에서 유저불러오기 에러")
            error(e)
        }
    }

    override suspend fun getSchedule(id: String, month: String): Result<Schedule> {
        try {
            val response = apiService.getSchedule(id, month)
            return when (response.code()) {
                200 -> {
                    val entity = response.body()!!
                    val schedule = ScheduleMapper.from(entity)
                    Result.Success(schedule)
                }
                404 -> {
                    Result.Error(Error("찾는 Key 정보가 없음"))
                }
                else -> {
                    Result.Error(Throwable("서버에러"))
                }
            }
        } catch (e: Throwable) {
            return Result.Error(e)
        }
    }

    override suspend fun getCompanies(): List<Company> {
        return try {
            val companyEntity = localDataSource.getCompanies()
            val company = companyEntity.map {
                CompanyMapper.from(it)
            }
            company
        } catch (e: Exception) {
            Log.e(TAG, "getCompanies: Room에서 유저불러오기 에러")
            error(e)
        }
    }

    // SET //
    ////////////////////////////////////////////////////////////////
    override suspend fun setUser(info: Map<String, String>): APIResult {
        return try {
            val json = Json.encodeToString(info)
            val response = apiService.setUser(json)
            when {
                response.code() == 200 -> {
                    Log.e(TAG, "setUser: ${response.body()}")
                    APIResult.Success
                }
                else -> handleError(response.code(), "setUser")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "setUser : ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun setCompany(info: Map<String, String>): APIResult {
        return try {
            val json = Json.encodeToString(info)
            val response = apiService.setCompany(json)
            when {
                response.code() == 200 -> APIResult.Success
                else -> handleError(response.code(), "setCompany")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "setCompany: ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun setSchedule(schedule: Schedule): APIResult {
        return try {
            val entity = ScheduleMapper.to(schedule)
            val json = Json.encodeToString(entity)
            Log.e(TAG, "setSchedule: ${json}")
            val response = apiService.setSchedule(json)
            when {
                response.code() == 200 -> APIResult.Success
                else -> handleError(response.code(), "setSchedule")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "setSchedule: ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    // UPDATE //
    ////////////////////////////////////////////////////////////////
    override suspend fun updateUser(query: Map<String, String>): APIResult {
        return try {
            val json = Json.encodeToString(query)
            val response = apiService.updateUser(json)
            when {
                response.code() == 200 -> APIResult.Success
                else -> handleError(response.code(), "updateUser")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "updateUser: ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun handleError(code: Int, tag: String): APIResult.Error {
        return when (code) {
            404 -> {
                Log.e(TAG, "$tag : 찾는 key정보가 없음")
                APIResult.Error(APIError.KeyValueError)
            }
            500 -> {
                Log.e(TAG, "$tag : 서버에러")
                APIResult.Error(APIError.SeverError)
            }
            else -> {
                Log.e(TAG, "$tag : 알수없는 statusCode")
                APIResult.Error(APIError.Error(Throwable("설정하지 않은 http code가 날라옴 ")))
            }
        }
    }
    
    companion object {
        val TAG = "DataRepositoryImpl"
    }
}