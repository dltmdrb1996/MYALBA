package com.bottotop.core.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottotop.core.model.Event

open class BaseViewModel(private val name : String) : ViewModel() {

    protected val TAG = name

//    private val _isLoading by lazy { MutableLiveData(false) }
//    val isLoading: LiveData<Boolean> by lazy { _isLoading }
private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        Log.e(TAG, ":뷰모델생성 ", )
    }

    private val _toast by lazy { MutableLiveData<Event<String>>() }
    val toast: LiveData<Event<String>> by lazy { _toast }

    fun handleLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    fun showToast(message: String) {
        _toast.postValue(Event(message))
    }

    override fun onCleared() {
        Log.e(TAG, "onCleared: 종료", )
        super.onCleared()
    }
}