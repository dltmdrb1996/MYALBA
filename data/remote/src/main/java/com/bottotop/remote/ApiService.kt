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

import com.bottotop.remote.entity.CompaniesEntity
import com.bottotop.remote.entity.ResponseResult
import com.bottotop.remote.entity.ScheduleEntity
import com.bottotop.remote.entity.UserEntity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("user")
    suspend fun getUser(
        @Query("id") id : String
    ) : Response<UserEntity>

    @POST("user")
    suspend fun setUser(
        @Query("user") user : String
    ) : Response<ResponseBody>

    @PUT("user")
    suspend fun updateUser(
        @Query("query") query : String ,
    ) : Response<ResponseBody>

    @GET("companies")
    suspend fun getCompanies(
        @Query("id") id : String
    ) : Response<CompaniesEntity>

    @POST("companies")
    suspend fun setCompany(
        @Query("company") company : String
    ) : Response<ResponseBody>

    @POST("schedule")
    suspend fun setSchedule(
        @Query("schedule") schedule : String
    ) : Response<ResponseBody>

    @GET("schedule")
    suspend fun getSchedule(
        @Query("id") id : String,
        @Query("month") month : String,
    ) : Response<ScheduleEntity>

    @PUT("schedule")
    suspend fun updateSchedule(
        @Query("query") query : String ,
    ) : Response<ResponseBody>
}
