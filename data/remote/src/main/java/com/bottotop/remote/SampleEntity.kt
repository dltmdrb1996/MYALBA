package com.bottotop.remote

import com.google.gson.annotations.Expose
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SampleEntity(
    @SerialName("statusCode")
    val statusCode : Int ,
    @SerialName("body")
    val body : String,
    @SerialName("lsg")
    val lsg : String
)
