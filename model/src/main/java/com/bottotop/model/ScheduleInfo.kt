package com.bottotop.model

data class ScheduleInfo(
    val day : String,
    val content : ScheduleContent
)

data class ScheduleContent(
    val startTime : String,
    val endTime : String,
    val workTime : String,
)

