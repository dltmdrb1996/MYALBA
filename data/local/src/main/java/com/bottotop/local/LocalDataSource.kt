package com.bottotop.local

import com.bottotop.local.entity.LocalCommunityEntity
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.local.entity.NotificationEntity
import com.bottotop.model.DaySchedule

interface LocalDataSource {
    suspend fun insertUser(user: LocalUserEntity)
    suspend fun getUser(id: String): LocalUserEntity
    suspend fun getMember(): List<LocalUserEntity>
    suspend fun deleteMember(id : String)

    suspend fun insertCompany(company : LocalCompanyEntity)
    suspend fun getCompany(id: String): LocalCompanyEntity
    suspend fun getCompanies(): List<LocalCompanyEntity>
    suspend fun nukeCompany()

    suspend fun insertNotification(notificationEntity: NotificationEntity)
    suspend fun nukeNotification()
    suspend fun getNotification() : List<NotificationEntity>

    suspend fun deleteDaySchedule()
    suspend fun insertDaySchedule(day : String , time : String)
    suspend fun getDaySchedule() : DaySchedule

    suspend fun getCommunity() : List<LocalCommunityEntity>
    suspend fun insertCommunity(localCommunityEntity: LocalCommunityEntity)

    suspend fun nukeAll()
}