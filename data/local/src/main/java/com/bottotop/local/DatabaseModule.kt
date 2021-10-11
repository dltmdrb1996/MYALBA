package com.bottotop.local

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private const val DB_NAME = "sample.db"

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): SampleDatabase {
        return Room.databaseBuilder(context, SampleDatabase::class.java, DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideSampleDao(database: SampleDatabase) : SampleDao {
        return database.sampleDao()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(source: LocalDataSourceImpl): LocalDataSource {
        return source
    }
}