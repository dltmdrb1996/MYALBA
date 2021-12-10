package com.bottotop.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottotop.core.model.Event
import com.bottotop.model.wrapper.APIError
import com.bottotop.model.wrapper.APIResult
import timber.log.Timber
import java.sql.Time

open class BaseViewModel(name : String) : ViewModel() {

    protected val TAG = name

//    private val _isLoading by lazy { MutableLiveData(false) }
//    val isLoading: LiveData<Boolean> by lazy { _isLoading }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _toast by lazy { MutableLiveData<Event<String>>() }
    val toast: LiveData<Event<String>> by lazy { _toast }

    fun handleLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    fun showToast(message: String) {
        _toast.postValue(Event(message))
    }

    fun APIResult.result(stack : Array<out StackTraceElement>) : Boolean{
        return when(this){
            is APIResult.Success -> true
            is APIResult.Error -> {
                getAPIError(stack[0] , this.error)
            }
            else -> false
        }
    }

    private fun getAPIError(stack : StackTraceElement ,error : APIError) : Boolean{
        when(error){
            is APIError.SeverError -> {
                Timber.tag(stack.fileName).e("[${stack.lineNumber}]  [${stack.className.split("$")[1]}]  :  서버에러발생")
                showToast("서버에러가 발생하였습니다.")
            }
            is APIError.KeyValueError -> {
                Timber.tag(stack.fileName).e("[${stack.lineNumber}]  [${stack.className.split("$")[1]}]  :  KeyValueError")
            }
            is APIError.NullValueError -> {
                Timber.tag(stack.fileName).e("[${stack.lineNumber}]  [${stack.className.split("$")[1]}]  :  NullValueError")
                showToast("데이터가 존재하지 않습니다.")
            }
            is APIError.Error ->  {
                Timber.tag(stack.fileName).e("[${stack.lineNumber}]  [${stack.className.split("$")[1]}]  : ${error.e}")
                showToast("에러가 발생하였습니다.")
            }
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("$TAG 종료")
    }
}