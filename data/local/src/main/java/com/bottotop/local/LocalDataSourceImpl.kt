package com.bottotop.local

import com.bottotop.local.dao.CompanyDao
import com.bottotop.local.dao.UserDao
import com.bottotop.local.entity.DayScheduleEntity
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.local.entity.NotificationEntity
import com.bottotop.model.DaySchedule
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val companyDao: CompanyDao
) : LocalDataSource {

    override suspend fun insertUser(user: LocalUserEntity) {
        userDao.insert(user)
    }

    override suspend fun getUser(id : String): LocalUserEntity = userDao.getUser(id)

    override suspend fun getMember(): List<LocalUserEntity> = userDao.getMember()

    override suspend fun deleteMember(id: String) {
        userDao.deleteMember(id)
    }

    override suspend fun insertCompany(company : LocalCompanyEntity) {
        companyDao.insert(company)
    }

    override suspend fun getCompany(id: String): LocalCompanyEntity = companyDao.getCompany(id)

    override suspend fun getCompanies(): List<LocalCompanyEntity> = companyDao.getCompanies()

    override suspend fun nukeCompany() {
        companyDao.deleteCompany()
    }

    override suspend fun insertNotification(notificationEntity: NotificationEntity) {
        userDao.insertNotification(notificationEntity)
    }

    override suspend fun nukeNotification() {
        userDao.nukeNotification()
    }

    override suspend fun deleteDaySchedule() {
        userDao.deleteSchedule()
    }

    override suspend fun getNotification(): List<NotificationEntity> = userDao.getNotification()

    override suspend fun insertDaySchedule(day : String , time : String) {
        userDao.insertSchedule(DayScheduleEntity(day, time))
    }

    override suspend fun getDaySchedule(): DaySchedule {
        val entity = userDao.getSchedule()
        return DaySchedule(entity.day,entity.time)
    }

    override suspend fun nukeAll() {
        userDao.deleteUser()
        userDao.deleteSchedule()
        nukeCompany()
        userDao.nukeNotification()
    }
}