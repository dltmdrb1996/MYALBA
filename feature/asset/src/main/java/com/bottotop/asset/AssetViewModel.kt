package com.bottotop.asset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor() : BaseViewModel("자원뷰모델") {

    init {
        handleLoading(false)
        Log.e(TAG, "뷰모델시작")
    }

    fun clickTest(){
        Log.e(TAG, "clickTest: 됨?", )
    }

    fun test(){
        showToast("test")
    }
}