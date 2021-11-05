package com.bottotop.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleContentEntity(
    @SerialName("startTime")
    val startTime : String,
    @SerialName("endTime")
    val endTime : String,
    @SerialName("workTime")
    val workTime : String,
)
