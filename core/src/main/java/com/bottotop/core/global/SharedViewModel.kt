package com.bottotop.core.global

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bottotop.core.base.BaseViewModel
import com.bottotop.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
) : BaseViewModel("메인뷰모델") {

    private val _network = MutableLiveData<Boolean>()
    val network : LiveData<Boolean> = _network

    private val _user = MutableLiveData<User>()
    val user = _user

    fun changeNetworkState(state : Boolean){
        _network.value = state
    }

}