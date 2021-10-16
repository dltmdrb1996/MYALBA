package com.bottotop.local

import com.bottotop.local.dao.UserDao
import com.bottotop.local.entity.LocalUserEntity
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : LocalDataSource {

    override suspend fun insertSample(localUser: LocalUserEntity) {
        userDao.insert(localUser)
    }

    override suspend fun getAllSamples() = userDao.getUser()
}