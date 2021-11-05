package com.bottotop.model.repository

import com.bottotop.model.*
import com.bottotop.model.wrapper.APIResult

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
    suspend fun getCommunity(com_id : String) : Result<List<Community>>

    //set
    suspend fun setCompany(info : Map<String,String>): APIResult
    suspend fun setUser(info : Map<String,String>) : APIResult
    suspend fun setSchedule(id_month : Map<String,String>) : APIResult
    suspend fun setCommunity(com_id_id_name_content_time : Map<String,String>) : APIResult

    //update
    suspend fun updateUser(id_target_change : Map<String,String>) : APIResult
    suspend fun updateSchedule(id_target_day_startTime : Map<String,String>) : APIResult
    suspend fun patchSchedule(id_month_dat_target_change : Map<String,String>) : APIResult

    //local
    suspend fun getDaySchedule() : DaySchedule
    suspend fun insertDaySchedule(day : String , time :String)
    suspend fun deleteDaySchedule()


    suspend fun handleError(code : Int , tag : String) : APIResult.Error

}