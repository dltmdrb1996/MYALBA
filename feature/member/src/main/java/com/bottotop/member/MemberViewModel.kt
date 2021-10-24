package com.bottotop.member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("맴버뷰모델") {

    private val _sample = MutableLiveData<User>()
    val sample: LiveData<User> = _sample

    init {
        test()
        handleLoading(false)
        Log.e(TAG, "뷰모델시작")
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun test() {
        viewModelScope.launch(dispatcherProvider.io) {
            _sample.postValue(dataRepository.getUser())
            dataRepository.updateUser(
                mapOf(
                    Pair("id", SocialInfo.id),
                    Pair("target", "com_id"),
                    Pair("change", SocialInfo.id)
                )
            )
        }
    }

    fun setUser() {
        viewModelScope.launch(dispatcherProvider.io) {
            dataRepository.setUser(
                mapOf<String, String>(
                    Pair("id", SocialInfo.id),
                    Pair("tel", SocialInfo.tel),
                    Pair("birth", SocialInfo.birth),
                    Pair("name", SocialInfo.name),
                    Pair("email", SocialInfo.email),
                )
            )
        }
    }

    suspend fun setCompany() {
        val query : Map<String,String> = mapOf(
            Pair("id", SocialInfo.id),
            Pair("target", "com_id"),
            Pair("change" , SocialInfo.social)
        )
        dataRepository.updateUser(query)

    }

}