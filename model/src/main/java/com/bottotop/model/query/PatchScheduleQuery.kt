package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class PatchScheduleQuery(
    val id: String,
    val month: String,
    val day: String,
    val target: String,
    val change: String,
    val target2: String,
    val change2: String,
    val target3: String,
    val change3: String,
)
