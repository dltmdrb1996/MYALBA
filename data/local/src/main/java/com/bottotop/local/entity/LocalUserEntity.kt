package com.bottotop.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class LocalUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "pk")
    val pk: String,
    @ColumnInfo(name = "birth")
    val birth: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "tell")
    val tel: String,
    @ColumnInfo(name = "com_id")
    val com_id: String,
    @ColumnInfo(name = "social")
    val social: String,
)