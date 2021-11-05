package com.bottotop.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.util.DateTime
import com.bottotop.model.*
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("커뮤니티뷰모델") {

    private val _communityList = MutableLiveData<List<Community>>()
    val communityList: LiveData<List<Community>> = _communityList

    private val _success = MutableLiveData<Boolean>()
    val success = _success

    private val dateUtil = DateTime()
    val content = MutableLiveData<String>()
    lateinit var user: User

    init {
        try {
            viewModelScope.launch(dispatcherProvider.io) {
                user = dataRepository.getUser(SocialInfo.id)
                init()
            }
        } catch (e: Throwable) {
            Log.e(TAG, "룸데이터 불러오기 오류 : $e ")
        }
    }

    fun init() {
        viewModelScope.launch(dispatcherProvider.io){
            val getCommunity = dataRepository.getCommunity(user.company)
            if(getCommunity.isSuccess) _communityList.postValue(getCommunity.getOrNull())
            else Log.e(TAG, "makeCommunity: getCommunity", )
        }
    }


    fun makeCommunity() {
        viewModelScope.launch(dispatcherProvider.io) {
            val setCommunity = getAPIResult(dataRepository.setCommunity(
                mapOf(
                    Pair("com_id", user.company),
                    Pair("id", SocialInfo.id),
                    Pair("name", user.name),
                    Pair("content", content.value!!),
                    Pair("time", dateUtil.getCurrentDateFromStringFormat()),
                )
            ),"$TAG : setCommunity")

            if(setCommunity){
                content.postValue("")
                val getCommunity = dataRepository.getCommunity(user.company)
                if(getCommunity.isSuccess) {
                    _communityList.postValue(getCommunity.getOrNull())
                    _success.postValue(true)
                }
                else Log.e(TAG, "makeCommunity: getCommunity", )
            }
        }
    }

    fun clearContent(){
        content.value = ""
    }
}