package com.bottotop.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottotop.local.entity.LocalUserEntity

@Dao
internal interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: LocalUserEntity)

    @Query("SELECT * FROM user")
    fun getUser(): LocalUserEntity
}