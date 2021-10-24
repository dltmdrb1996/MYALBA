package com.bottotop.local

import com.bottotop.local.dao.UserDao
import com.bottotop.local.entity.LocalUserEntity
import javax.inject.Inject

internal class LocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao
) : LocalDataSource {

    override suspend fun insertUser(user: LocalUserEntity) {
        userDao.insert(user)
    }

    override suspend fun getUser(): LocalUserEntity = userDao.getUser()

}