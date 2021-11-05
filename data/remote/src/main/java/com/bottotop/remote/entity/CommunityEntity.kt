package com.bottotop.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CommunityListEntity(
    @SerialName("community")
    val community : List<CommunityEntity> = emptyList()
)

@Serializable
data class CommunityEntity(
    val id : String,
    val name : String,
    val content : String,
    val time : String,
    val comment : List<CommentEntity> = emptyList()
)

@Serializable
data class CommentEntity(
    val id : String,
    val name : String,
    val time : String,
    val content : String,
)