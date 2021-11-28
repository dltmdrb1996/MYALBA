package com.bottotop.repository

import com.bottotop.remote.ApiService
import com.bottotop.remote.entity.*
import okhttp3.ResponseBody
import retrofit2.Response

class FakeRemote : ApiService {
    override suspend fun getUser(id: String): Response<UserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun setUser(user: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(query: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun getCompanies(id: String): Response<CompaniesEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun setCompany(company: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun setSchedule(query: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun getSchedule(id: String, month: String): Response<ScheduleEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updateSchedule(query: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDetailSchedule(query: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSchedule(query: String): Response<ScheduleGetAllEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun setCommunity(query: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommunity(query: String): Response<CommunityListEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getCommunityDetail(query: String): Response<CommunityEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun setComment(query: String): Response<ResponseBody> {
        TODO("Not yet implemented")
    }
}