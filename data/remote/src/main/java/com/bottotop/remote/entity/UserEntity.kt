package com.bottotop.remote.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    @SerialName("PK")
    val id: String =  "",
    @SerialName("tel")
    val tel: String = "",
    @SerialName("birth")
    val birth: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("com_id")
    val com_id: String = "",
    @SerialName("social")
    val social: String = "",

)
