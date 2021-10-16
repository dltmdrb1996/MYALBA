package com.bottotop.repository

import com.bottotop.model.repository.SocialLoginRepository
import com.bottotop.remote.login.KakaoDataSource
import com.bottotop.remote.login.NaverDataSource
import javax.inject.Inject

internal class SocialLoginRepositoryImpl @Inject constructor(
    private val kakaoDataSource: KakaoDataSource,
    private val naverDataSource: NaverDataSource
) : SocialLoginRepository {

    override suspend fun checkNaver() = naverDataSource.checkToken()

    override suspend fun getNaverInfo() = naverDataSource.loadUser()

    override suspend fun logoutNaver() {
        naverDataSource.logout()
    }

    override suspend fun disconectNaver() {
        naverDataSource.disConnect()
    }

    override suspend fun refreshNaver() {
        naverDataSource.refresh()
    }


    override suspend fun checkKakao() = kakaoDataSource.checkToken()

    override suspend fun getKakaoInfo() = kakaoDataSource.loadUser()

    override suspend fun loagoutKakao() {
        kakaoDataSource.logout()
    }

    override suspend fun disconectKakao() {
        kakaoDataSource.disConnect()
    }

    override suspend fun refreshKakao() {
        kakaoDataSource.refresh()
    }


}