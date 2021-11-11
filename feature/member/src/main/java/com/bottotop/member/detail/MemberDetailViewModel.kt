package com.bottotop.member.detail


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.Company
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberDetailViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel("맴버디테일모델") {

    var userId: String = savedStateHandle.get<String>("msg")!!

    private val _user = MutableLiveData<User>()
    val user = _user

    private val _company = MutableLiveData<Company>()
    val company = _company

    private val _workDay = MutableLiveData<String>()
    val workDay = _workDay


    init {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                handleLoading(true)
                val user = dataRepository.getUser(userId)
                _user.postValue(user)
                val company = dataRepository.getCompany(userId)
                _company.postValue(company)
                getMemberWorkDay(company)
            } catch (e: Throwable) {
                showToast("데이터를 불러오는데 실패했습니다.")
                Log.e(TAG, "룸데이터 불러오기 에러 : $e")
            }
            handleLoading(false)
        }
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

}