package com.bottotop.repository.mapper

import com.bottotop.local.entity.LocalCommunityEntity
import com.bottotop.local.entity.LocalCompanyEntity
import com.bottotop.model.Comment
import com.bottotop.model.Community
import com.bottotop.model.Company
import com.bottotop.remote.entity.CommentEntity
import com.bottotop.remote.entity.CommunityEntity
import com.bottotop.remote.entity.CompanyEntity

internal object CommunityMapper : Mapper<CommunityEntity, Community>() {

    override fun from(from: CommunityEntity): Community {
        return Community(
            id = from.id,
            name = from.name,
            content = from.content,
            time = from.time,
            idx =from.idx,
            comment = from.comment.map { CommentMapper.from(it) }
        )
    }

    override fun to(to: Community): CommunityEntity {
        return CommunityEntity(
            id = to.id,
            name = to.name,
            content = to.content,
            time = to.time,
            idx = to.idx,
            comment = to.comment.map { CommentMapper.to(it) }
        )
    }
}

internal object CommentMapper : Mapper<CommentEntity, Comment>() {

    override fun from(from: CommentEntity): Comment {
        return Comment(
            id = from.id,
            name = from.name,
            content = from.content,
            time = from.time
        )
    }

    override fun to(to: Comment): CommentEntity {
        return CommentEntity(
            id = to.id,
            name = to.name,
            content = to.content,
            time = to.time
        )
    }
}

internal object LocalCommunityMapper : Mapper<LocalCommunityEntity, Community>() {

    override fun from(from: LocalCommunityEntity): Community {
        return Community(
            id = from.id,
            name = from.name,
            content = from.content,
            time = from.time,
            idx =from.idx,
            comment = from.comment
        )
    }

    override fun to(to: Community): LocalCommunityEntity {
        return LocalCommunityEntity(
            id = to.id,
            name = to.name,
            content = to.content,
            time = to.time,
            idx = to.idx,
            comment = to.comment
        )
    }
}

