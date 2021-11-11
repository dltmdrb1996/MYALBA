package com.bottotop.model

import kotlinx.serialization.Serializable

@Serializable
data class Community(
    val id : String,
    val name : String,
    val content : String,
    val time : String,
    val idx : String,
    val comment : List<Comment>
)

@Serializable
data class Comment(
    val id : String,
    val name : String,
    val time : String,
    val content : String,
)