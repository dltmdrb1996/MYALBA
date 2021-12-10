package com.bottotop.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottotop.local.entity.DayScheduleEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.local.entity.NotificationEntity

@Dao
internal interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: LocalUserEntity)

    @Query("SELECT * FROM user")
    fun getMember(): List<LocalUserEntity>

    @Query("SELECT * FROM user WHERE pk LIKE (:id)")
    fun getUser(id : String): LocalUserEntity

    @Query("DELETE FROM user WHERE pk NOT LIKE (:id)")
    fun deleteMember(id : String)

    @Query("DELETE FROM user")
    fun deleteUser()

    @Query("DELETE FROM schedule")
    fun deleteSchedule()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(obj: NotificationEntity)

    @Query("DELETE FROM notification")
    fun nukeNotification()

    @Query("SELECT * FROM notification")
    fun getNotification(): List<NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(obj : DayScheduleEntity)

    @Query("SELECT * FROM schedule")
    fun getSchedule() : DayScheduleEntity


}