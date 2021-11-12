package com.bottotop.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.LocalUserEntity

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


}