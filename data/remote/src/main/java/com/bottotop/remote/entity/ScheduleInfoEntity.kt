package com.bottotop.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfoEntity(
    @SerialName("d")
    val day : String,
    @SerialName("content")
    val content : ScheduleContentEntity
)


