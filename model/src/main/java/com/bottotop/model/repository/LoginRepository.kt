package com.bottotop.model.repository

interface LoginRepository {

    suspend fun checkNaver() : Boolean
    suspend fun getNaverInfo() : Boolean
    suspend fun logoutNaver()
    suspend fun disconectNaver()
    suspend fun refreshNaver()

    suspend fun checkKakao() : Boolean
    suspend fun getKakaoInfo() : Boolean
    suspend fun loagoutKakao()
    suspend fun disconectKakao()
    suspend fun refreshKakao()

}