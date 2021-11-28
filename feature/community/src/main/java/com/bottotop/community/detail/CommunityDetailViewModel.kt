package com.bottotop.community.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.core.util.DateTime
import com.bottotop.model.*
import com.bottotop.model.query.SetCommentQuery
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository,
    savedStateHandle: SavedStateHandle

) : BaseViewModel("커뮤니티디테일뷰모델") {

    private val dateUtil = DateTime()
    private var userId: String = savedStateHandle.get<String>("msg")!!
    private val arg = Json.decodeFromString<Community>(userId)

    private val _community = MutableLiveData<Community>()
    val community : LiveData<Community> = _community

    private val _comment = MutableLiveData<List<Comment>>()
    val comment : LiveData<List<Comment>> = _comment

    val content = MutableLiveData<String>()
    private lateinit var user : User

    init {
        viewModelScope.launch(dispatcherProvider.io){
            handleLoading(true)
            try {
                initCommunityDetail()
            }catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Timber.e(": $e")
            }
            handleLoading(false)
        }
    }

    private suspend fun initCommunityDetail(){
        user = dataRepository.getUser(SocialInfo.id)
        val getCommunityDetail = dataRepository.getCommunityDetail(
            mapOf(Pair("com_id", user.company), Pair("idx", arg.idx))
        )
        if (getCommunityDetail.isSuccess){
            val community = getCommunityDetail.getOrNull()!!
            _community.postValue(community)
            _comment.postValue(community.comment)
        }
    }

    fun makeComment(){
        viewModelScope.launch(dispatcherProvider.io){
            handleLoading(true)
            val setComment = dataRepository.setComment(
                SetCommentQuery(user.company , arg.idx , user.id , user.name , content.value!! , dateUtil.getCurrentDateFromStringFormat())
            ).result(Error().stackTrace)
            if(setComment){
                val getCommunityDetail = dataRepository.getCommunityDetail(
                    mapOf(Pair("com_id", user.company), Pair("idx", arg.idx))
                )
                if(getCommunityDetail.isSuccess){
                    val community = getCommunityDetail.getOrNull()!!
                    _comment.postValue(community.comment)
                    _community.postValue(community)
                    content.postValue("")
                } else {
                    Timber.e("makeComment: 불러오기실패 ${getCommunityDetail.exceptionOrNull()}")
                }
            }
            handleLoading(false)
        }
    }
}