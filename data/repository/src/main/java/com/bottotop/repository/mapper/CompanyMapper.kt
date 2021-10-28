package com.bottotop.repository.mapper

import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.model.Company
import com.bottotop.remote.entity.CompanyEntity

internal object CompanyEntityMapper : Mapper<CompanyEntity, LocalCompanyEntity>() {
    override fun from(from: CompanyEntity): LocalCompanyEntity {
        return LocalCompanyEntity(
            pk = from.PK,
            pay = from.pay,
            com_tel = from.com_tel,
            com_name = from.com_name,
            position = from.position,
            com_id = from.com_id,
            workday = from.workday,
            start = from.start,
            end = from.end
        )
    }

    override fun to(to: LocalCompanyEntity): CompanyEntity {
        return CompanyEntity(
            PK = to.pk,
            pay = to.pay,
            com_tel = to.com_tel,
            com_name = to.com_name,
            position = to.position,
            com_id = to.com_id,
            workday = to.workday,
            start = to.start,
            end = to.end
        )
    }

}

internal object CompanyMapper : Mapper<LocalCompanyEntity, Company>() {
    override fun from(from: LocalCompanyEntity): Company {
        return Company(
            PK = from.pk,
            pay = from.pay,
            com_tel = from.com_tel,
            com_name = from.com_name,
            position = from.position,
            com_id = from.com_id,
            workday = from.workday,
            start = from.start,
            end = from.end
        )
    }

    override fun to(to: Company): LocalCompanyEntity {
        return LocalCompanyEntity(
            pk = to.PK,
            pay = to.pay,
            com_tel = to.com_tel,
            com_name = to.com_name,
            position = to.position,
            com_id = to.com_id,
            workday = to.workday,
            start = to.start,
            end = to.end
        )
    }

}