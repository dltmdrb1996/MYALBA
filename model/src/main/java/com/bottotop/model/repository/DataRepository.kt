package com.bottotop.model.repository

import com.bottotop.model.Company
import com.bottotop.model.User
import com.bottotop.model.wrapper.APIResult
import com.bottotop.model.wrapper.Result

interface DataRepository {

    //로컬에 갱신된 데이터저장
    //refresh
    suspend fun refreshUser(id : String) : APIResult
    suspend fun refreshCompanies(id : String) : APIResult

    // 로컬에서 데이터를 불러옴
    //get
    suspend fun getUser(id : String) : User
    suspend fun getCompanies() : List<Company>
    suspend fun getMembers() : List<User>
    suspend fun getCompany(id : String) : Company

    //set
    suspend fun setCompany(info : Map<String,String>): APIResult
    suspend fun setUser(info : Map<String,String>) : APIResult

    //update
    suspend fun updateUser(id_target_change : Map<String,String>) : APIResult

    suspend fun handleError(code : Int , tag : String) : APIResult.Error
}