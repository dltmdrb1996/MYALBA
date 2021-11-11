package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class SetScheduleQuery(
    val id: String,
    val month: String,
    val com_id: String,

    )
