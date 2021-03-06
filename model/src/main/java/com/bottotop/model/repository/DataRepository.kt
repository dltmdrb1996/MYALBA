package com.bottotop.model.repository

import com.bottotop.model.*
import com.bottotop.model.query.*
import com.bottotop.model.wrapper.APIResult

interface DataRepository {

    //refresh
    suspend fun refreshUser(id : String) : APIResult
    suspend fun refreshCompanies(id : String) : APIResult
    suspend fun refreshCommunity(com_id : String) : APIResult

    //get
    suspend fun getUser(id : String) : User
    suspend fun getCompanies() : List<Company>
    suspend fun getMembers() : List<User>
    suspend fun getCompany(id : String) : Company
    suspend fun getSchedule(id : String , month : String) : Result<Schedule>
    suspend fun getScheduleAll(com_id_id : Map<String,String>) : Result<List<Schedule>>
    suspend fun getCommunity() : Result<List<Community>>

    //set
    suspend fun setCompany(setCompanyQuery: SetCompanyQuery): APIResult
    suspend fun setUser(setUserQuery: SetUserQuery) : APIResult
    suspend fun setSchedule(setScheduleQuery: SetScheduleQuery) : APIResult
    suspend fun setCommunity(setCommunityQuery: SetCommunityQuery) : APIResult
    suspend fun setComment(setCommentQuery: SetCommentQuery) : APIResult

    //update
    suspend fun updateUser(updateUserQuery: UpdateUserQuery) : APIResult
    suspend fun updateSchedule(updateScheduleQuery: UpdateScheduleQuery) : APIResult
    suspend fun patchSchedule(patchScheduleQuery : PatchScheduleQuery) : APIResult

    //delete
    suspend fun deleteALL(id : String) : APIResult

    //local
    suspend fun getDaySchedule() : Result<DaySchedule>
    suspend fun insertDaySchedule(day : String , time :String)
    suspend fun deleteDaySchedule()
    suspend fun deleteAllTable()
    suspend fun getNotification() : Result<List<Notification>>
    suspend fun insertNotification(notification: Notification)
    suspend fun nukeNotification()

    suspend fun handleError(code : Int , tag : String) : APIResult.Error

}