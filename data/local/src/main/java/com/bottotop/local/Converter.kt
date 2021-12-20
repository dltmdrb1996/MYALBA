package com.bottotop.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bottotop.model.Comment
import com.bottotop.model.Community
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class Converter {

//    @TypeConverter
//    fun fromCommunity(value : List<Community>) = Json.encodeToString(value)
//    @TypeConverter
//    fun toCommunity(value: String) = Json.decodeFromString<List<Community>>(value)


    @TypeConverter
    fun fromComment(value : List<Comment>) = Json.encodeToString(value)
    @TypeConverter
    fun toComment(value: String) = Json.decodeFromString<List<Comment>>(value)
}