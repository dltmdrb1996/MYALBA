package com.bottotop.community

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
import com.bottotop.model.query.UpdateUserQuery
import com.bottotop.model.repository.DataRepository
import com.bottotop.model.wrapper.APIError
import com.bottotop.model.wrapper.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("커뮤니티뷰모델") {

    private val _communityList = MutableLiveData<List<Community>>()
    val communityList: LiveData<List<Community>> = _communityList

    private val _success by lazy { MutableLiveData<Event<Community>>() }
    val success: LiveData<Event<Community>> by lazy { _success }

    private val _failure by lazy { MutableLiveData<Event<Boolean>>() }
    val failure: LiveData<Event<Boolean>> by lazy { _failure }

    private val _bottomLoading = MutableLiveData<Boolean>()
    val bottomLoading : LiveData<Boolean> = _bottomLoading

    private val dateUtil = DateTime()
    val content = MutableLiveData<String>()



    private lateinit var user: User

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            user = dataRepository.getUser(SocialInfo.id)
        }
    }

    fun initCommunity() {
        viewModelScope.launch(dispatcherProvider.io){
            handleLoading(true)
            try {
                val getCommunity = dataRepository.getCommunity()
                if(getCommunity.isSuccess) _communityList.postValue(getCommunity.getOrNull()?.reversed())
            } catch (e: Throwable) {
                showToast("데이터를 불러오는데 실패했습니다.")
                Timber.e("룸데이터 불러오기 오류 : $e")
            }
            handleLoading(false)
        }
    }


    fun makeCommunity() {
        viewModelScope.launch(dispatcherProvider.io) {
            _bottomLoading.postValue(true)
            val setCommunity = dataRepository.setCommunity(
                SetCommunityQuery(user.company , SocialInfo.id , user.name , content.value!! , dateUtil.getCurrentDateFromStringFormat())
            ).result(Error().stackTrace)

            if(setCommunity){
                content.postValue("")
                val refresh = dataRepository.refreshCommunity(user.company).result(Throwable().stackTrace)
                val community = dataRepository.getCommunity()
                if(refresh && community.isSuccess) {
                    _communityList.postValue(community.getOrNull()?.reversed())
                    _success.postValue(Event(community.getOrNull()?.last()!!))
                } else {
                    _failure.postValue(Event(true))
                }
            }
            _bottomLoading.postValue(false)
        }
    }

    fun clearContent(){
        content.value = ""
    }
}