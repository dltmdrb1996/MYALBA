package com.bottotop.local

import android.content.Context
import androidx.room.Room
import com.bottotop.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private const val DB_NAME = "myjob.db"

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MyjobDatabase {
        return Room.databaseBuilder(context, MyjobDatabase::class.java, DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideSampleDao(database: MyjobDatabase) : UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(source: LocalDataSourceImpl): LocalDataSource {
        return source
    }
}