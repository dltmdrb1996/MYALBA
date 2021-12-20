package com.bottotop.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bottotop.model.Comment

@Entity(tableName = "community")
data class LocalCommunityEntity(
    @PrimaryKey
    @ColumnInfo(name = "idx")
    val idx: String,
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "comment")
    val comment: List<Comment>
)

