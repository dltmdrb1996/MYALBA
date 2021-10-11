package com.bottotop.repository.mapper

import com.bottotop.model.Sample
import com.bottotop.remote.SampleEntity
import com.bottotop.repository.Mapper

internal object SampleEntityMapper : Mapper<SampleEntity, Sample>() {
    override fun from(from: SampleEntity): Sample {
        return Sample(
            statusCode = from.statusCode,
            body = from.body,
            lsg = from.lsg
        )
    }

    override fun to(to: Sample): SampleEntity {
        return SampleEntity(
            statusCode = to.statusCode,
            body = to.body,
            lsg = to.lsg
        )
    }
}