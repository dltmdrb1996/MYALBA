package com.bottotop.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleEntity(
    @SerialName("id")
    val id : String,
    @SerialName("month")
    val month : String,
    @SerialName("scheduleInfo")
    val scheduleInfo : List<ScheduleInfoEntity> = emptyList()
)
