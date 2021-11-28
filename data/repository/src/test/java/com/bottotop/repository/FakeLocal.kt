package com.bottotop.repository

import com.bottotop.local.LocalDataSource
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.model.DaySchedule

class FakeLocal : LocalDataSource {
    override suspend fun insertUser(user: LocalUserEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(id: String): LocalUserEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getMember(): List<LocalUserEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMember(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertCompany(company: LocalCompanyEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun getCompany(id: String): LocalCompanyEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getCompanies(): List<LocalCompanyEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun nukeCompany() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDaySchedule() {
        TODO("Not yet implemented")
    }

    override suspend fun insertDaySchedule(day: String, time: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getDaySchedule(): DaySchedule {
        TODO("Not yet implemented")
    }

    override suspend fun nukeAll() {
        TODO("Not yet implemented")
    }
}