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
        viewModelScope.launch(dispatcherProvider.io){
            dataRepository.getSchedule(SocialInfo.id,"10")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }


}