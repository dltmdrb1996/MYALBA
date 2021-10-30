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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("가입뷰모델") {

    private val _albaComplete = MutableLiveData<Boolean>()
    val albaComplete : LiveData<Boolean> = _albaComplete

    private val _managerComplete = MutableLiveData<Boolean>()
    val managerComplete : LiveData<Boolean> = _managerComplete

    val code = MutableLiveData<String>()
    val checkMon = MutableLiveData<Boolean>(false)
    val checkTus = MutableLiveData<Boolean>(false)
    val checkWed = MutableLiveData<Boolean>(false)
    val checkThu = MutableLiveData<Boolean>(false)
    val checkFri = MutableLiveData<Boolean>(false)
    val checkSat = MutableLiveData<Boolean>(false)
    val checkSun = MutableLiveData<Boolean>(false)
    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()

    fun setCompany() {

        if(checkAlbaInform()) return

        viewModelScope.launch(dispatcherProvider.io) {
            handleLoading(true)
            val success = getAPIResult(dataRepository.refreshCompanies(code.value!!))
            if (success) {
                val companies = dataRepository.getCompanies()
                val company = companies[0]
                updateUserCompany(code.value!!)

                company.apply {
                    val companySuccess = getAPIResult(dataRepository.setCompany(
                        mapOf<String, String>(
                            Pair("id", SocialInfo.id),
                            Pair("com_id", com_id),
                            Pair("com_name", com_name),
                            Pair("com_tel", com_tel),
                            Pair("pay", pay),
                            Pair("position", "B"),
                            Pair("start", startTime.value!!),
                            Pair("end", endTime.value!!),
                            Pair("workday", checkWeek()),
                        )
                    ))

                    if(companySuccess){
                        val refresh = getAPIResult(dataRepository.refreshCompanies(code.value!!))
                        if(refresh){
                            Log.e(TAG, "setCompany: test", )
                            handleLoading(false)
                            _albaComplete.postValue(true)
                        }else{
                            Log.e(TAG, "setCompany: testfaile", )
                            _albaComplete.postValue(false)
                        }
                    }else{
                        _albaComplete.postValue(false)
                    }
                }
            }
            handleLoading(false)
        }
    }

    private suspend fun updateUserCompany(id: String) {
        val success = getAPIResult(
            dataRepository.updateUser(
                mapOf(
                    Pair("id", SocialInfo.id),
                    Pair("target", "com_id"),
                    Pair("change", id)
                )
            )
        )
        if (success) {
            dataRepository.refreshUser(SocialInfo.id)
            Log.e(TAG, "updateUserCompany: ${dataRepository.getUser(SocialInfo.id)}")
        }
    }

    fun checkWeek(): String {
        var workBinary : String =""
        val list = listOf(
            checkMon.value,
            checkTus.value,
            checkWed.value,
            checkThu.value,
            checkFri.value,
            checkSat.value,
            checkSun.value
        )
        list.forEach {
            workBinary += if(it==true) "1" else "0"
        }
        return workBinary
    }

    fun checkAlbaInform() : Boolean{
        val workBinary = checkWeek()
        if (code.value.isNullOrEmpty()) {
            showToast("코드를 입력해주세요")
            return true
        }
        if (!workBinary.contains('1')) {
            showToast("요일을 체크해주세요")
            return true
        }
        if (startTime.value.isNullOrEmpty() || endTime.value.isNullOrEmpty()) {
            showToast("시간을 입력해주세요")
            return true
        }
        if(startTime.value!!.first()=='0' ){
            startTime.postValue(startTime.value!![1].toString())
        }
        if( endTime.value!!.first()=='0') {
            endTime.postValue(endTime.value!![1].toString())
        }
        if(startTime.value!!.toInt() > 24 || endTime.value!!.toInt() >24){
            showToast("시간은 24시를 초과할수 없습니다")
            return true
        }
        return false
    }

    //////////////////// managerFragment ///////////////////

    val com_name = MutableLiveData<String>()
    val com_tel = MutableLiveData<String>()
    val pay = MutableLiveData<String>()

    fun makeCompany() {

        if(checkCompanyInform()) return

        viewModelScope.launch(dispatcherProvider.io) {
            handleLoading(true)
            updateUserCompany(SocialInfo.id)
            val success = getAPIResult(dataRepository.setCompany(
                mapOf<String, String>(
                    Pair("id", SocialInfo.id),
                    Pair("com_id", SocialInfo.id),
                    Pair("com_name", com_name.value!!),
                    Pair("com_tel",com_tel.value!!),
                    Pair("pay", pay.value!!),
                    Pair("position", "A"),
                    Pair("start","null"),
                    Pair("end","null"),
                    Pair("workday","null")
                )
            ))
            if(success) {
                val refresh = getAPIResult(dataRepository.refreshCompanies(SocialInfo.id))
                if(refresh) {
                    handleLoading(false)
                    _managerComplete.postValue(true)
                }else{
                    _managerComplete.postValue(false)
                }
            }else{
                _managerComplete.postValue(false)
            }
            handleLoading(false)
        }
    }

    private fun checkCompanyInform() : Boolean{
        if (com_name.value.isNullOrEmpty()) {
            showToast("가게이름을 입력해주세요")
            return true
        }
        if (com_tel.value.isNullOrEmpty()) {
            showToast("번호를 입력해주세요")
            return true
        }
        if(com_tel.value!!.length>=13){
            showToast("번호는 12자리를 넘어갈수없습니다")
            return true
        }
        if(pay.value.isNullOrEmpty()){
            showToast("시급을 입력해주세요")
            return true
        }
        return false
    }
}