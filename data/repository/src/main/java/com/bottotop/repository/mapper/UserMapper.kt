package com.bottotop.repository.mapper

import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.model.User
import com.bottotop.remote.entity.UserEntity

internal object UserEntityMapper : Mapper<UserEntity, LocalUserEntity>() {

    override fun from(from: UserEntity): LocalUserEntity {
        return LocalUserEntity(
            pk = from.id,
            tel = from.tel,
            birth = from.birth,
            name = from.name,
            email = from.email,
            com_id = from.com_id,
            social = from.social
        )
    }

    override fun to(to: LocalUserEntity): UserEntity {
        return UserEntity(
            id = to.pk,
            tel = to.tel,
            birth = to.birth,
            name = to.name,
            email = to.email,
            com_id = to.com_id,
            social = to.social
        )
    }
}

internal object UserMapper : Mapper<LocalUserEntity, User>() {

    override fun from(from: LocalUserEntity): User {
        return User(
            id = from.pk,
            tel = from.tel,
            birth = from.birth,
            name = from.name,
            email = from.email,
            company = from.com_id,
            social = from.social
        )
    }

    override fun to(to: User): LocalUserEntity {
        return LocalUserEntity(
            pk = to.id,
            tel = to.tel,
            birth = to.birth,
            name = to.name,
            email = to.email,
            com_id = to.company,
            social = to.social
        )
    }
}