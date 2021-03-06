/*
 * Copyright 2021 SOUP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bottotop.remote

import com.bottotop.remote.entity.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /// 유저 ////////////////////////////////////////////

    @GET("user")
    suspend fun getUser(
        @Query("id") id: String
    ): Response<UserEntity>

    @POST("user")
    suspend fun setUser(
        @Query("user") user: String
    ): Response<ResponseBody>

    @PUT("user")
    suspend fun updateUser(
        @Query("query") query: String
    ): Response<ResponseBody>

    @DELETE("user")
    suspend fun deleteUser(
        @Query("query") id: String
    ): Response<ResponseBody>

    /// 컴패니 ////////////////////////////////////////////

    @GET("companies")
    suspend fun getCompanies(
        @Query("id") id: String
    ): Response<CompaniesEntity>

    @POST("companies")
    suspend fun setCompany(
        @Query("company") company: String
    ): Response<ResponseBody>

    /// 스케쥴 ////////////////////////////////////////////

    @POST("schedule")
    suspend fun setSchedule(
        @Query("query") query: String
    ): Response<ResponseBody>

    @GET("schedule")
    suspend fun getSchedule(
        @Query("id") id: String,
        @Query("month") month: String
    ): Response<ScheduleEntity>

    @PUT("schedule")
    suspend fun updateSchedule(
        @Query("query") query: String,
    ): Response<ResponseBody>

    @PATCH("schedule")
    suspend fun updateDetailSchedule(
        @Query("query") query: String
    ): Response<ResponseBody>

    @GET("schedule/all")
    suspend fun getAllSchedule(
        @Query("query") query: String
    ): Response<ScheduleGetAllEntity>

    /// 커뮤니티 ////////////////////////////////////////////

    @POST("community")
    suspend fun setCommunity(
        @Query("query") query: String
    ): Response<ResponseBody>

    @GET("community")
    suspend fun getCommunity(
        @Query("com_id") query: String
    ): Response<CommunityListEntity>

    @GET("community/detail")
    suspend fun getCommunityDetail(
        @Query("query") query: String
    ): Response<CommunityEntity>

    @POST("community/comment")
    suspend fun setComment(
        @Query("query") query: String
    ): Response<ResponseBody>
}
