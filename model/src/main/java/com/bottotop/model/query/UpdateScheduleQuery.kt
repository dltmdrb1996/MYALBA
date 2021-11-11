package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class UpdateScheduleQuery(
    val startTime: String,
    val id: String,
    val month: String,
    val day: String,
    val workTime: String,
)
