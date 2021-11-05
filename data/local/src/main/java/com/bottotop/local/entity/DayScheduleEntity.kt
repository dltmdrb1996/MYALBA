package com.bottotop.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class DayScheduleEntity(
    @PrimaryKey
    @ColumnInfo(name = "day")
    val day: String,
    @ColumnInfo(name = "time")
    val time: String,

)