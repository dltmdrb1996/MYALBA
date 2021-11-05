package com.bottotop.model

data class Community(
    val id : String,
    val name : String,
    val content : String,
    var time : String,
    val comment : List<Comment>
)

data class Comment(
    val id : String,
    val name : String,
    val time : String,
    val content : String,
)