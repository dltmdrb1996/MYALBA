package com.bottotop.model.query

import kotlinx.serialization.Serializable

@Serializable
data class SetCompanyQuery(
  val  id : String,
  val  com_id : String,
  val  com_name : String,
  val  com_tel : String,
  val  pay : String,
  val  position : String,
  val  start : String,
  val  end : String,
  val  workday : String,
)
