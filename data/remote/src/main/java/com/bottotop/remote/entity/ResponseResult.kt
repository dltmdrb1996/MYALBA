package com.bottotop.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class ResponseResult(
    val code : String,
    val msg : String
)
