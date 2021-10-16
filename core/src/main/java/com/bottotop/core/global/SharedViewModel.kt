package com.bottotop.core.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottotop.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
) : BaseViewModel("메인뷰모델") {

    private val _test = MutableLiveData<String>()
    val sample : LiveData<String> = _test

    private val _network = MutableLiveData<Boolean>()
    val network : LiveData<Boolean> = _network

    fun changeNetworkState(state : Boolean){
        _network.value = state
    }

    fun test(str : String){
        _test.value = str
    }
}