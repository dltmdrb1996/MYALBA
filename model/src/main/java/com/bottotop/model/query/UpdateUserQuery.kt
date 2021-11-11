package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserQuery(
  val  id : String,
  val  target  : String,
  val  change  : String,
)
