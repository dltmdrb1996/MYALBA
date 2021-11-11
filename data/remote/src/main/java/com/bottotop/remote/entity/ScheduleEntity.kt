package com.bottotop.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleGetAllEntity(
    val scheduleEntity : List<ScheduleEntity>
)

@Serializable
data class ScheduleEntity(
    @SerialName("PK")
    val id : String,
    @SerialName("SK")
    val month : String,
    @SerialName("scheduleInfo")
    val scheduleInfo : List<ScheduleInfoEntity> = emptyList()
)
