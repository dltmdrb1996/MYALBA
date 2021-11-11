package com.bottotop.member

data class MemberModel(
    val id : String,
    val name : String,
    val workOn : String,
    val age : String,
    val tel : String,
    val email : String,
    val pay : String,
    val color : List<Int>,
    val position : String
)
