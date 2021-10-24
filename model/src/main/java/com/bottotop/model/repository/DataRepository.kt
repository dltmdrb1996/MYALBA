package com.bottotop.model.repository

import com.bottotop.model.APIResult
import com.bottotop.model.Company
import com.bottotop.model.Sample
import com.bottotop.model.User
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun getUser() : User
    suspend fun setUser(info : Map<String,String>) : APIResult
    suspend fun updateUser(id_target_change : Map<String,String>)
    suspend fun refreshUser(id : String) : APIResult
    suspend fun refreshAndGetUser(id : String) : User

    suspend fun getCompanies(id : String) : List<Company>
    suspend fun setCompany(info : Map<String,String>) : APIResult
}