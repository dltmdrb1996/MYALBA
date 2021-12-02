package com.bottotop.register.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.model.Event
import com.bottotop.core.util.DateTime
import com.bottotop.model.query.SetCompanyQuery
import com.bottotop.model.query.SetScheduleQuery
import com.bottotop.model.query.UpdateUserQuery
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("가입뷰모델") {

    private val _albaComplete by lazy { MutableLiveData<Event<Boolean>>() }
    val albaComplete : LiveData<Event<Boolean>> by lazy { _albaComplete }

    private val _managerComplete by lazy { MutableLiveData<Event<Boolean>>() }
    val managerComplete : LiveData<Event<Boolean>> by lazy { _managerComplete }

    val code = MutableLiveData<String>()
    val checkMon = MutableLiveData(false)
    val checkTus = MutableLiveData(false)
    val checkWed = MutableLiveData(false)
    val checkThu = MutableLiveData(false)
    val checkFri = MutableLiveData(false)
    val checkSat = MutableLiveData(false)
    val checkSun = MutableLiveData(false)
    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()

    fun setCompany() {

        if(checkAlbaInform()) return

        viewModelScope.launch(dispatcherProvider.io) {
            handleLoading(true)
            val refreshCompanies1 = dataRepository.refreshCompanies(code.value!!).result(Throwable().stackTrace)
            if (refreshCompanies1) {
                val companies = dataRepository.getCompanies()
                val company = companies[0]
                if(!updateUserCompany(code.value!!)) return@launch
                company.apply {
                    val setCompany = dataRepository.setCompany(
                        SetCompanyQuery(SocialInfo.id,com_id,com_name,com_tel,pay,"B",
                            startTime.value!!,endTime.value!!,checkWeek())
                        ).result(Throwable().stackTrace)

                    if(setCompany){
                        dataRepository.setSchedule(SetScheduleQuery(SocialInfo.id , DateTime().getYearMonth() , com_id))
                        val refreshCompanies1_2 = dataRepository.refreshCompanies(code.value!!).result(Throwable().stackTrace)
                        if(refreshCompanies1_2){
                            handleLoading(false)
                            _albaComplete.postValue(Event(true))
                        }
                        else _albaComplete.postValue(Event(false))
                    }
                    else _albaComplete.postValue(Event(false))
                }
            } else {
                _albaComplete.postValue(Event(false))
            }
            handleLoading(false)
        }
    }

    private suspend fun updateUserCompany(change : String) : Boolean {
        val updateUser =
            dataRepository.updateUser(
                UpdateUserQuery(SocialInfo.id,"com_id",change)
            ).result(Error().stackTrace)
        return if (updateUser) dataRepository.refreshUser(SocialInfo.id).result(Error().stackTrace)
        else false

    }

    private fun checkWeek(): String {
        var workBinary = ""
        listOf(checkMon.value, checkTus.value, checkWed.value, checkThu.value,
            checkFri.value, checkSat.value, checkSun.value
        ).forEach { workBinary += if(it==true) "1" else "0" }
        return workBinary
    }

    private fun checkAlbaInform() : Boolean{
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
        if(startTime.value!!.first()=='0' ) {
            startTime.postValue(startTime.value!![1].toString())
        }
        if(endTime.value!!.first()=='0') {
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

            if(!updateUserCompany(SocialInfo.id)) return@launch

            val setCompany2 = dataRepository.setCompany(
                SetCompanyQuery(SocialInfo.id,SocialInfo.id,com_name.value!!,com_tel.value!!,
                pay.value!!,"A","","","0000000")
            ).result(Error().stackTrace)
            if(setCompany2) {
                val refreshCompanies2 = dataRepository.refreshCompanies(SocialInfo.id).result(Error().stackTrace)

                if(refreshCompanies2) {
                    handleLoading(false)
                    _managerComplete.postValue(Event(true))
                }else{
                    _managerComplete.postValue(Event(false))
                }
            }else{
                _managerComplete.postValue(Event(false))
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