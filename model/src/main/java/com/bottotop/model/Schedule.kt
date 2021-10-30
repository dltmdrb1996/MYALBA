package com.bottotop.model

data class Schedule(
    val id : String,
    val month : String,
    val scheduleInfo : List<ScheduleInfo>
)
