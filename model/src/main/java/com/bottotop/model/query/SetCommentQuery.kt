package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class SetCommentQuery(
    val com_id: String,
    val idx: String,
    val id: String,
    val name: String,
    val content: String,
    val time: String,
)
