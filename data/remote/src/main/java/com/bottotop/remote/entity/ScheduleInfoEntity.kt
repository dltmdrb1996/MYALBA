package com.bottotop.remote.entity

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInfoEntity(
    @SerializedName("day")
    val day : String,
    @SerializedName("startTime")
    val startTime : String,
    @SerializedName("endTime")
    val endTime : String,
    @SerializedName("workTime")
    val workTime : String,
)
