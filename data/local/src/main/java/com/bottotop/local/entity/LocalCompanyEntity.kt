package com.bottotop.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company")
data class LocalCompanyEntity(
    @PrimaryKey
    @ColumnInfo(name = "pk")
    val pk: String,
    @ColumnInfo(name = "com_tel" )
    val com_tel: String,
    @ColumnInfo(name = "com_name" )
    val com_name: String,
    @ColumnInfo(name = "com_id" )
    val com_id: String,
    @ColumnInfo(name = "workday" )
    val workday: String,
    @ColumnInfo(name = "pay" )
    val pay: String,
    @ColumnInfo(name = "position" )
    val position: String,
    @ColumnInfo(name = "start" )
    val start: String,
    @ColumnInfo(name = "end" )
    val end: String,
)

