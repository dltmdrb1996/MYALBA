package com.bottotop.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bottotop.local.dao.CompanyDao
import com.bottotop.local.dao.UserDao
import com.bottotop.local.entity.DayScheduleEntity
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.local.entity.NotificationEntity

@Database(
    entities = [
        LocalUserEntity::class,
        LocalCompanyEntity::class,
        DayScheduleEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class MyjobDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao
}