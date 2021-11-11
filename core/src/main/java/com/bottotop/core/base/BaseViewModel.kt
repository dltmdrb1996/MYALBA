package com.bottotop.core.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottotop.core.model.Event
import com.bottotop.model.APIError
import com.bottotop.model.wrapper.APIResult

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

    fun getAPIResult(result : APIResult, tag : String) : Boolean{
        return when(result){
            is APIResult.Success -> true
            is APIResult.Error -> getAPIError(result.error,tag)
        }
    }

    fun getAPIError(error : APIError , tag : String) : Boolean{
        when(error){
            is APIError.SeverError -> {
                Log.e(TAG, "$tag -> SeverError", )
                showToast("서버접속이 원할하지 않습니다 잠시후 다시 실행해주세요.")
            }
            is APIError.KeyValueError ->{
                Log.e(TAG, "$tag -> KeyValueError", )
                showToast("해당하는 데이터가 존재하지 않습니다")
            }
            is APIError.NullValueError ->{
                Log.e(TAG, "$tag -> NullValueError", )
                showToast("데이터가 없습니다.")
            }
            is APIError.Error -> {
                Log.e(TAG, "$tag -> ${error.e}", )
                showToast("에러가 발생했습니다. 잠시후 다시 실행해주세요")
            }
        }
        return false
    }

    override fun onCleared() {
        Log.e(TAG, "onCleared: 종료", )
        super.onCleared()
    }

}