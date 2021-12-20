package com.bottotop.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bottotop.local.dao.CompanyDao
import com.bottotop.local.dao.UserDao
import com.bottotop.local.entity.*

@Database(
    entities = [
        LocalUserEntity::class,
        LocalCompanyEntity::class,
        DayScheduleEntity::class,
        NotificationEntity::class,
        LocalCommunityEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converter::class)
internal abstract class MyjobDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun companyDao(): CompanyDao

}