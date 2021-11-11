package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class SetCommunityQuery(
    val com_id: String,
    val id: String,
    val name: String,
    val content: String,
    val time: String,
)
