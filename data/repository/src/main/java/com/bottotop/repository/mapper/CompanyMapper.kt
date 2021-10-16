package com.bottotop.repository.mapper

import com.bottotop.model.Company
import com.bottotop.remote.entity.CompanyEntity
import com.bottotop.repository.Mapper

internal object CompanyMapper : Mapper<CompanyEntity, Company>() {
    override fun from(from: CompanyEntity): Company {
        return Company(
            PK = from.PK,
            SK = from.SK,
            pay = from.pay,
            address = from.address,
            com_tel = from.com_tel,
            position = from.position,
            com_id = from.com_id
        )
    }

    override fun to(to: Company): CompanyEntity {
        return CompanyEntity(
            PK = to.PK,
            SK = to.SK,
            pay = to.pay,
            address = to.address,
            com_tel = to.com_tel,
            position = to.position,
            com_id = to.com_id
        )
    }

}