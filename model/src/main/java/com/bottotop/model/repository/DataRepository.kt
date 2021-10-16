package com.bottotop.model.repository

import com.bottotop.model.Company
import com.bottotop.model.Sample
import com.bottotop.model.User
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    suspend fun getUser(id : String) : User
    suspend fun setUser(info : Map<String,String>)
    suspend fun updateUser(query : Map<String,String>)

    suspend fun getCompanies(id : String) : List<Company>

}