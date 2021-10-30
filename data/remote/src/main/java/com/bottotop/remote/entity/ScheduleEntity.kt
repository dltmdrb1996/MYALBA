package com.bottotop.remote.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleEntity(
    @SerializedName("id")
    val id : String,
    @SerializedName("month")
    val month : String,
    @SerializedName("scheduleInfo")
    val scheduleInfo : List<ScheduleInfoEntity>
)
