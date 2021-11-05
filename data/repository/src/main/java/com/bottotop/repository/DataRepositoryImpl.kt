package com.bottotop.repository

import android.util.Log
import com.bottotop.local.LocalDataSource
import com.bottotop.model.*
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.wrapper.APIResult
import com.bottotop.remote.ApiService
import com.bottotop.repository.mapper.*
import com.bottotop.repository.mapper.CommunityMapper
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
            Log.e(TAG, "refreshCompanies: ${response.code()}", )
            when {
                response.code() == 200 -> {
                    if (response.body() == null) return APIResult.Error(APIError.NullValueError)
                    response.body()!!.companies.forEach {
                        val company = CompanyEntityMapper.from(it)
                        localDataSource.insertCompany(company)
                        val getUser = apiService.getUser(it.PK)
                        if (getUser.code() == 200) {
                            val user = UserEntityMapper.from(getUser.body()!!)
                            localDataSource.insertUser(user)
                        } else {
                            handleError(getUser.code(),"getUser in refreshUser")
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


    // GET //
    ////////////////////////////////////////////////////////////////
    override suspend fun getUser(id: String): User {
        try {
            val user = UserMapper.from(localDataSource.getUser(id))
            return user
        } catch (e: Exception) {
            Log.e(TAG, "getUser: Room에서 유저불러오기 에러")
            return error(e)
        }
    }

    override suspend fun getMembers(): List<User> {
        try {
            val memberEntity = localDataSource.getMember()
            val member = memberEntity.map {
                UserMapper.from(it)
            }
            return member
        } catch (e: Exception) {
            Log.e(TAG, "getMembers: Room에서 유저불러오기 에러")
            return error(e)
        }
    }

    override suspend fun getCompany(id: String): Company {
        return try {
            val memberEntity = localDataSource.getCompany(id)
            val member = CompanyMapper.from(memberEntity)
            member
        } catch (e: Exception) {
            Log.e(TAG, "getCompany: Room에서 유저불러오기 에러")
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
                    Result.success(schedule)
                }
                404 -> {
                    Log.e(TAG, "getSchedule: 404에러", )
                    Result.failure(Throwable("찾는 데이터가 없습니다."))
                }
                else -> {
                    Log.e(TAG, "getSchedule: 서버에러", )
                    Result.failure(Throwable("서버에러"))
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, "getSchedule: 에러발생 ${e.message}", )
            return Result.failure(e)
        }
    }

    override suspend fun getCommunity(com_id: String): Result<List<Community>> {
        try {
            val response = apiService.getCommunity(com_id)
            return when (response.code()) {
                200 -> {
                    val entity = response.body()!!
                    val schedule = entity.community.map { CommunityMapper.from(it) }
                    Result.success(schedule)
                }
                404 -> {
                    Log.e(TAG, "getSchedule: 404에러", )
                    Result.failure(Throwable("찾는 데이터가 없습니다."))
                }
                else -> {
                    Log.e(TAG, "getSchedule: 서버에러", )
                    Result.failure(Throwable("서버에러"))
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, "getSchedule: 에러발생 ${e.message}", )
            return Result.failure(e)
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

    override suspend fun setSchedule(query : Map<String,String>): APIResult {
        return try {
            val json = Json.encodeToString(query)
            val response = apiService.setSchedule(json)
            when {
                response.code() == 200 -> APIResult.Success
                else -> APIResult.Error(APIError.Error(Throwable("이미만들어진상태")))
            }
        } catch (e: Throwable) {
            Log.e(TAG, "setSchedule: ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun setCommunity(query : Map<String, String>): APIResult {
        return try {
            val json = Json.encodeToString(query)
            val response = apiService.setCommunity(json)
            when {
                response.code() == 200 -> APIResult.Success
                else -> handleError(response.code(), "setCommunity")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "setCommunity: ${e}")
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

    override suspend fun updateSchedule(query : Map<String,String>): APIResult {
        return try {
            val json = Json.encodeToString(query)
            val response = apiService.updateSchedule(json)
            when {
                response.code() == 200 -> APIResult.Success
                response.code() == 300 -> {
                    val setSchedule = apiService.setSchedule(json)
                    return when(setSchedule.code()){
                        200-> {
                            val setUpdate =apiService.updateSchedule(json)
                            if (setUpdate.code()==200)APIResult.Success
                            else handleError(setUpdate.code(),"retry updateSchedule")
                        }
                        else -> handleError(setSchedule.code(),"setSchedule in updateSchedule")
                    }
                }
                else -> handleError(response.code(), "updateSchedule")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "updateSchedule: ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun patchSchedule(query: Map<String, String>): APIResult {
        return try {
            val json = Json.encodeToString(query)
            val response = apiService.updateDetailSchedule(json)
            when {
                response.code() == 200 -> APIResult.Success
                else -> handleError(response.code(), "patchSchedule")
            }
        } catch (e: Throwable) {
            Log.e(TAG, "patchSchedule: ${e}")
            APIResult.Error(APIError.Error(e))
        }
    }

    override suspend fun getDaySchedule(): DaySchedule {
        return localDataSource.getDaySchedule()
    }

    override suspend fun insertDaySchedule(day : String , time : String) {
        localDataSource.insertDaySchedule(day , time)
    }

    override suspend fun deleteDaySchedule() {
        localDataSource.getDaySchedule()
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
                Log.e(TAG, "$tag : 알수없는 statusCode , 코드 : ${code}")
                APIResult.Error(APIError.Error(Throwable("설정하지 않은 http code가 날라옴 ")))
            }
        }
    }

    companion object {
        val TAG = "DataRepositoryImpl"
    }
}