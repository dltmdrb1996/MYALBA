package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class SetUserQuery(
  val  id : String,
  val  tel : String,
  val  birth  : String,
  val  name : String,
  val  email  : String,
  val  social : String,
  val  com_id : String,
  val  workOn : String,
)
