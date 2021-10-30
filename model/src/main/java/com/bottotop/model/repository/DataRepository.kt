package com.bottotop.model.repository

import com.bottotop.model.Company
import com.bottotop.model.Schedule
import com.bottotop.model.User
import com.bottotop.model.wrapper.APIResult
import com.bottotop.model.wrapper.Result

interface DataRepository {

    //refresh
    suspend fun refreshUser(id : String) : APIResult
    suspend fun refreshCompanies(id : String) : APIResult

    //get
    suspend fun getUser(id : String) : User
    suspend fun getCompanies() : List<Company>
    suspend fun getMembers() : List<User>
    suspend fun getCompany(id : String) : Company
    suspend fun getSchedule(id : String , month : String) : Result<Schedule>

    //set
    suspend fun setCompany(info : Map<String,String>): APIResult
    suspend fun setUser(info : Map<String,String>) : APIResult
    suspend fun setSchedule(schedule : Schedule) : APIResult

    //update
    suspend fun updateUser(id_target_change : Map<String,String>) : APIResult
    suspend fun updateSchedule(id_target_scheduleInfo : Map<String,String>) : APIResult


    suspend fun handleError(code : Int , tag : String) : APIResult.Error
}