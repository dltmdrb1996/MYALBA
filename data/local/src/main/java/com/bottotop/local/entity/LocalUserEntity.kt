package com.bottotop.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class LocalUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "pk" , defaultValue = "user")
    val pk: String,
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "birth" , defaultValue = "")
    val birth: String,
    @ColumnInfo(name = "email" , defaultValue = "")
    val email: String,
    @ColumnInfo(name = "name" , defaultValue = "")
    val name: String,
    @ColumnInfo(name = "tell" , defaultValue = "")
    val tel: String,
    @ColumnInfo(name = "com_id" , defaultValue = "")
    val com_id: String,
)