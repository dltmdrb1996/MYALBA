package com.bottotop.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottotop.local.entity.DayScheduleEntity
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.model.DaySchedule
import com.bottotop.model.User

interface LocalDataSource {
    suspend fun insertUser(user: LocalUserEntity)
    suspend fun getUser(id: String): LocalUserEntity
    suspend fun getMember(): List<LocalUserEntity>
    suspend fun deleteMember(id : String)

    suspend fun insertCompany(company : LocalCompanyEntity)
    suspend fun getCompany(id: String): LocalCompanyEntity
    suspend fun getCompanies(): List<LocalCompanyEntity>
    suspend fun nukeCompany()

    suspend fun deleteDaySchedule()
    suspend fun insertDaySchedule(day : String , time : String)
    suspend fun getDaySchedule() : DaySchedule
}