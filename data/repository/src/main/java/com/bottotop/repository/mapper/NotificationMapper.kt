package com.bottotop.repository.mapper

import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.local.entity.NotificationEntity
import com.bottotop.model.Company
import com.bottotop.model.Notification
import com.bottotop.remote.entity.CompanyEntity

internal object NotificationMapper : Mapper<NotificationEntity, Notification>() {
    override fun from(from: NotificationEntity): Notification {
        return Notification(
            title = from.title,
            time = from.time,
            content = from.content
        )
    }

    override fun to(to: Notification): NotificationEntity {
        return NotificationEntity(
            title = to.title,
            time = to.time,
            content = to.content
        )
    }

}
