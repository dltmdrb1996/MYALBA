package com.bottotop.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.Company
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.repository.SocialLoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle,
    private val socialLoginRepository: SocialLoginRepository
) : BaseViewModel("마이페이지뷰모델") {

    var userId: String = savedStateHandle.get<String>("msg")!!

    private val _user = MutableLiveData<User>()
    val user : LiveData<User> = _user

    private val _company = MutableLiveData<Company>()
    val company : LiveData<Company> = _company

    private val _workDay = MutableLiveData<String>()
    val workDay: LiveData<String> = _workDay

    private val _navLogin = MutableLiveData<Boolean>()
    val navLogin : LiveData<Boolean> = _navLogin

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                initFirst()
            } catch (e: Throwable) {
                showToast("데이터를 불러오는데 실패했습니다.")
                Log.e(TAG, "룸데이터 불러오기 에러 : $e")
            }
        }
    }

    private suspend fun initFirst(){
        val user = dataRepository.getUser(userId)
        _user.postValue(user)
        val company = dataRepository.getCompany(userId)
        _company.postValue(company)
        getMemberWorkDay(company)
    }

    private suspend fun getMemberWorkDay(company: Company) {
        var workday = ""
        company.workday.forEachIndexed { index, c ->
            if (c == '1') {
                when (index) {
                    0 -> workday += "월요일 "
                    1 -> workday += "화요일 "
                    2 -> workday += "수요일 "
                    3 -> workday += "목요일 "
                    4 -> workday += "금요일 "
                    5 -> workday += "토요일 "
                    6 -> workday += "일요일 "
                }
            }
        }
        _workDay.postValue(workday)
    }

    fun unRegister(){
        viewModelScope.launch(dispatcherProvider.io){
            if(user.value!!.social == "naver") socialLoginRepository.disconectNaver()
            else socialLoginRepository.disconectKakao()
            dataRepository.deleteAllTable()
            val deleteUser = getAPIResult(dataRepository.deleteALL(SocialInfo.id),"$TAG unRegister")
            if(deleteUser){
                _navLogin.postValue(true)
            }
        }
    }
}