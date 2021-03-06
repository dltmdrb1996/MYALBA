package com.bottotop.repository.mapper

import com.bottotop.model.Schedule
import com.bottotop.model.ScheduleContent
import com.bottotop.model.ScheduleInfo
import com.bottotop.remote.entity.ScheduleContentEntity
import com.bottotop.remote.entity.ScheduleEntity
import com.bottotop.remote.entity.ScheduleInfoEntity

internal object ScheduleMapper : Mapper<ScheduleEntity, Schedule>() {
    override fun from(from: ScheduleEntity): Schedule {
        return Schedule(
            id = from.id,
            month = from.month,
            scheduleInfo = from.scheduleInfo.map {
                ScheduleInfoMapper.from(it)
            }
        )
    }

    override fun to(to: Schedule): ScheduleEntity {
        return ScheduleEntity(
            id = to.id,
            month = to.month,
            scheduleInfo = to.scheduleInfo.map {
                ScheduleInfoMapper.to(it)
            }
        )
    }
}


internal object ScheduleInfoMapper : Mapper<ScheduleInfoEntity, ScheduleInfo>() {
    override fun from(from: ScheduleInfoEntity): ScheduleInfo {
        return ScheduleInfo(
            day = from.day,
            content = ScheduleContentMapper.from(from.content)
        )
    }

    override fun to(to: ScheduleInfo): ScheduleInfoEntity {
        return ScheduleInfoEntity(
            day = to.day,
            content = ScheduleContentMapper.to(to.content),
        )
    }
}


internal object ScheduleContentMapper : Mapper<ScheduleContentEntity, ScheduleContent>() {
    override fun from(from: ScheduleContentEntity): ScheduleContent {
        return ScheduleContent(
            startTime = from.startTime,
            endTime = from.endTime,
            workTime = from.workTime,
            workPay = from.workPay
        )
    }

    override fun to(to: ScheduleContent): ScheduleContentEntity {
        return ScheduleContentEntity(
            startTime = to.startTime,
            endTime = to.endTime,
            workTime = to.workTime,
            workPay = to.workPay
        )
    }
}