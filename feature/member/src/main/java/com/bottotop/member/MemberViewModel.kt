package com.bottotop.member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.Sample
import com.bottotop.model.UserInfo
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("맴버뷰모델") {

    private val _sample = MutableLiveData<Sample>()
    val sample : LiveData<Sample> = _sample

    init {
        test()
        handleLoading(false)
        Log.e(TAG, "뷰모델시작")
    }

    override fun onCleared() {
        super.onCleared()
    }
    
    fun test(){
        viewModelScope.launch(dispatcherProvider.io) {
            _sample.postValue(dataRepository.getUser("LSGTEST" ,  "GGG"))
        }
    }
}