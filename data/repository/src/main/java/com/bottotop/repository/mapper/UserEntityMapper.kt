package com.bottotop.repository.mapper

import com.bottotop.local.entity.LocalUserEntity
import com.bottotop.model.User
import com.bottotop.remote.entity.UserEntity

internal object UserEntityMapper : Mapper<UserEntity, User>() {

    override fun from(from: UserEntity): User {
        return User(
            code = from.code,
            id = from.id,
            tel = from.tel,
            birth = from.birth,
            name = from.name,
            email = from.email,
            company = from.com_id
        )
    }

    override fun to(to: User): UserEntity {
        return UserEntity(
            code = to.code,
            id = to.id,
            tel = to.tel,
            birth = to.birth,
            name = to.name,
            email = to.email,
            com_id = to.company
        )
    }
}

internal object UserLocalEntityMapper : Mapper<LocalUserEntity, User>() {

    override fun from(from: LocalUserEntity): User {
        return User(
            code = from.code,
            id = from.id,
            tel = from.tel,
            birth = from.birth,
            name = from.name,
            email = from.email,
            company = from.com_id
        )
    }

    override fun to(to: User): LocalUserEntity {
        return LocalUserEntity(
            pk = "user",
            code = to.code,
            id = to.id,
            tel = to.tel,
            birth = to.birth,
            name = to.name,
            email = to.email,
            com_id = to.company
        )
    }
}