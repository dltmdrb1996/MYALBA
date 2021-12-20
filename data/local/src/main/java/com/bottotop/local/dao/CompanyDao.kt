package com.bottotop.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottotop.local.entity.LocalCommunityEntity
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.model.Community

@Dao
internal interface CompanyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: LocalCompanyEntity)

    @Query("SELECT * FROM company")
    fun getCompanies(): List<LocalCompanyEntity>

    @Query("SELECT * FROM company WHERE pk LIKE (:id)")
    fun getCompany(id : String): LocalCompanyEntity

    @Query("DELETE FROM company")
    fun deleteCompany()

    @Query("SELECT * FROM community")
    fun getCommunity(): List<LocalCommunityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCommunity(community: LocalCommunityEntity)

    @Query("DELETE FROM community")
    fun deleteCommunity()
}