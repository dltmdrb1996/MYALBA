package com.bottotop.register.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("가입뷰모델") {

    val code =  MutableLiveData<String>()
    val checkMon = MutableLiveData<Boolean>()
    val checkTus = MutableLiveData<Boolean>()
    val checkWed = MutableLiveData<Boolean>()
    val checkThu = MutableLiveData<Boolean>()
    val checkFri = MutableLiveData<Boolean>()
    val checkSat = MutableLiveData<Boolean>()
    val checkSun = MutableLiveData<Boolean>()
    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()


    init {
        handleLoading(false)
        viewModelScope.launch(dispatcherProvider.io){
            val user = dataRepository.getUser()
            Log.e(TAG, ": ${user}", )
        }
    }


    fun setCompany(){
        if(code.value.isNullOrEmpty()) {
            showToast("코드를 입력해주세요")
            return
        }
        if(!checkWeek()) {
            showToast("요일을 체크해주세요")
            return
        }
        if(startTime.value.isNullOrEmpty() || endTime.value.isNullOrEmpty()){
            showToast("시간을 입력해주세요")
            return
        }
        viewModelScope.launch(dispatcherProvider.io){
            val companies = dataRepository.getCompanies(code.value!!)
//            if(companies[0].code=="404"){
//                showToast("등록하려는 정보가 없습니다.")
//                return@launch
//            }
            val company = companies[0]
            Log.e(TAG, "setCompany: ${company}", )
            updateUserCompany(code.value!!)
            dataRepository.setCompany(
                mapOf<String, String>(
                    Pair("id", SocialInfo.id),
                    Pair("address", company.address),
                    Pair("com_id", company.com_id),
                    Pair("com_name", company.com_name),
                    Pair("com_tel", company.com_tel),
                    Pair("pay", company.pay),
                    Pair("position", "B"),
                    Pair("startTime", startTime.value!!),
                    Pair("endTime", endTime.value!!),
                    Pair("workDay", SocialInfo.email),
                )
            )
        }
    }

    suspend fun updateUserCompany(id : String) {
            dataRepository.updateUser(
                mapOf(
                    Pair("id", SocialInfo.id),
                    Pair("target", "com_id"),
                    Pair("change", id)
                )
            )
            if(dataRepository.refreshUser((SocialInfo.id))){
                Log.e(TAG, "updateUserCompany: 불러오기 성공", )
            } else {
                Log.e(TAG, "updateUserCompany: 유저갱신 실패", )
            }
            dataRepository.refreshUser(SocialInfo.id)
    }

    fun test(){
        Log.e(TAG, "test: ${code.value} ," +
                "${checkFri.value}," +
                "${checkMon.value}," +
                "${checkWed.value}," +
                "${startTime.value}," +
                "${endTime.value}", )
    }

    fun checkWeek() : Boolean{
        val list = listOf(checkMon.value , checkTus.value , checkWed.value
            , checkThu.value, checkFri.value, checkSat.value , checkSun.value)
        return list.contains(true)
    }
}