package com.bottotop.repository

abstract class Mapper<FROM, TO> {
    abstract fun from(from: FROM): TO
    abstract fun to(to: TO): FROM
}

