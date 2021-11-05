package com.bottotop.remote.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class CompaniesEntity(
    @Serializable(with = CompanyListSerializer::class)
    val companies: List<CompanyEntity>
)

@Serializable
data class CompanyEntity(
    val pay: String ="",
    val SK: String ="company",
    val com_tel: String,
    val com_name: String,
    val PK: String,
    val position: String,
    val com_id: String,
    val workday : String ="0000000",
    val start : String ="",
    val end : String ="",
)

object CompanyListSerializer :
    JsonTransformingSerializer<List<CompanyEntity>>(ListSerializer(CompanyEntity.serializer())) {
    // If response is not an array, then it is a single object that should be wrapped into the array
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element !is JsonArray) JsonArray(listOf(element)) else element
}