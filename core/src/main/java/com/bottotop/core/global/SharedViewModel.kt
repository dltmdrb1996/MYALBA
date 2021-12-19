package com.bottotop.core.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.Company
import com.bottotop.model.SocialData
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel("메인뷰모델") {

    private val _network = MutableLiveData<Boolean>()
    val network : LiveData<Boolean> = _network

    private val _homeInit = MutableLiveData<Boolean>()
    val homeInit : LiveData<Boolean> = _homeInit

    private val _notification = MutableLiveData<Int>()
    val notification = _notification

    lateinit var socialData : SocialData

    val user = MutableLiveData<User>()
    val Company = MutableLiveData<Company>()
    val member = MutableLiveData<List<User>>()
    val companies = MutableLiveData<List<Company>>()


    fun initData(){
    }

}