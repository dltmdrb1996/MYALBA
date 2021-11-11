package com.bottotop.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.model.Event
import com.bottotop.core.util.DateTime
import com.bottotop.model.*
import com.bottotop.model.query.SetCommunityQuery
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

    private val _success by lazy { MutableLiveData<Event<Boolean>>() }
    val success: LiveData<Event<Boolean>> by lazy { _success }

    private val _bottomLoading = MutableLiveData<Boolean>()
    val bottomLoading : LiveData<Boolean> = _bottomLoading

    private val dateUtil = DateTime()
    val content = MutableLiveData<String>()
    lateinit var user: User

    fun init() {
        viewModelScope.launch(dispatcherProvider.io){
            try {
                handleLoading(true)
                user = dataRepository.getUser(SocialInfo.id)
                val getCommunity = dataRepository.getCommunity(user.company)
                if(getCommunity.isSuccess) _communityList.postValue(getCommunity.getOrNull()?.reversed())
            } catch (e: Throwable) {
                showToast("데이터를 불러오는데 실패했습니다.")
                Log.e(TAG, "룸데이터 불러오기 오류 : $e ")
            }
            handleLoading(false)
        }
    }


    fun makeCommunity() {
        viewModelScope.launch(dispatcherProvider.io) {
            _bottomLoading.postValue(true)
            val setCommunity = getAPIResult(dataRepository.setCommunity(
                SetCommunityQuery(user.company , SocialInfo.id , user.name , content.value!! , dateUtil.getCurrentDateFromStringFormat())
            ),"$TAG : setCommunity")

            if(setCommunity){
                content.postValue("")
                val getCommunity = dataRepository.getCommunity(user.company)
                if(getCommunity.isSuccess) {
                    _communityList.postValue(getCommunity.getOrNull()?.reversed())
                    _success.postValue(Event(true))
                }
                else Log.e(TAG, "makeCommunity: getCommunity", )
            }
            _bottomLoading.postValue(false)
        }
    }

    fun clearContent(){
        content.value = ""
    }
}