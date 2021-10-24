package com.bottotop.local

import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.model.User

interface LocalDataSource {
    suspend fun insertUser(user : LocalUserEntity)
    suspend fun getUser(): LocalUserEntity
}