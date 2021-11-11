package com.bottotop.community.detail


import android.util.Log
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
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository,
    private val savedStateHandle: SavedStateHandle

) : BaseViewModel("커뮤니티디테일뷰모델") {

    val dateUtil = DateTime()
    var userId: String = savedStateHandle.get<String>("msg")!!
    val arg = Json.decodeFromString<Community>(userId)

    private val _community = MutableLiveData<Community>()
    val community : LiveData<Community> = _community

    private val _comment = MutableLiveData<List<Comment>>()
    val comment : LiveData<List<Comment>> = _comment

    val content = MutableLiveData<String>()
    lateinit var user : User
    lateinit var company : Company

    init {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io){
            try {
                user = dataRepository.getUser(SocialInfo.id)
                company = dataRepository.getCompany(arg.id)
                val getCommunityDetail = dataRepository.getCommunityDetail(
                    mapOf(Pair("com_id", company.com_id), Pair("idx", arg.idx))
                )
                if (getCommunityDetail.isSuccess){
                    val community = getCommunityDetail.getOrNull()!!
                    _community.postValue(community)
                    _comment.postValue(community.comment)
                }
            }catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Log.e(TAG, ": ${e}", )
            }
            handleLoading(false)
        }
    }

    fun makeComment(){
        viewModelScope.launch(dispatcherProvider.io){
            handleLoading(true)
            val setComment = getAPIResult(dataRepository.setComment(
                SetCommentQuery(user.company , arg.idx , user.id , user.name , content.value!! , dateUtil.getCurrentDateFromStringFormat())
            ),"$TAG : setComment")
            if(setComment){
                val getCommunityDetail = dataRepository.getCommunityDetail(
                    mapOf(Pair("com_id", company.com_id), Pair("idx", arg.idx))
                )
                if(getCommunityDetail.isSuccess){
                    val community = getCommunityDetail.getOrNull()!!
                    _comment.postValue(community.comment)
                    _community.postValue(community)
                    content.postValue("")
                } else {
                    Log.e(TAG, "makeComment: 불러오기실패 ${getCommunityDetail.exceptionOrNull()}", )
                }
            }
            handleLoading(false)
        }
    }
}