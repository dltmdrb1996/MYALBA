package com.bottotop.remote.login

import android.util.Log
import com.bottotop.model.UserInfo
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class KakaoDataSource @Inject constructor() {

    suspend fun checkToken(): Boolean = suspendCoroutine { cont ->
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { token, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        Log.e(TAG, "checkKakao: 유호하지않은토큰")
                    } else {
                        Log.e(TAG, "checkKakao: 기타에러")
                    }
                    cont.resume(false)
                } else {
                    Log.e(TAG, "카카오토큰 확인")
                    cont.resume(true)
                }
            }
        } else {
            Log.e(TAG, "토큰없음 로그인필요")
            cont.resume(false)
        }
    }

    suspend fun loadUser(): Boolean =
        suspendCoroutine { cont ->
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e(TAG, "사용자 정보 요청 실패", error)
                    cont.resume(false)
                } else if (user != null) {
                    Log.i(
                        TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}" +
                                "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"

                    )
                    // 현재 카카오 정보동의 미설정
                    UserInfo.name = "이승규"
                    UserInfo.mobile = "010"
                    UserInfo.email = "이메일"
                    UserInfo.birth = "1996"
                    cont.resume(true)
                }
            }
        }

    suspend fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }

    suspend fun disConnect() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            } else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }

    suspend fun refresh() {
        AuthApiClient.instance.refreshAccessToken { token, error -> }
    }

    companion object {
        val TAG = "KakaoDataSource"
    }

}