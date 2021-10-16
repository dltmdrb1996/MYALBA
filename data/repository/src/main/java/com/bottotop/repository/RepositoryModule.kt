package com.bottotop.repository

import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Singleton
    @Provides
    fun providesSampleRepository(repository: DataRepositoryImpl): DataRepository {
        return repository
    }

    @Singleton
    @Provides
    fun providesLoginRepository(repository: SocialLoginRepositoryImpl): SocialLoginRepository {
        return repository
    }

}