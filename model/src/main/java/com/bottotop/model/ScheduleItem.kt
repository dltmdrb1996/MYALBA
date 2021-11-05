package com.bottotop.model

data class ScheduleItem(
    val name : String,
    val workDay : List<String>,
    val start : String,
    val end : String
)
