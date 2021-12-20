package com.bottotop.community.detail

import androidx.lifecycle.*
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
    private var communityDetail = Json.decodeFromString<Community>(userId)

    private val _community = MutableLiveData<Community>()
    val community: LiveData<Community> = _community

    private val _comment = MutableLiveData<List<Comment>>()
    val comment: LiveData<List<Comment>> = _comment

    val content = MutableLiveData<String>()

    private lateinit var user: User

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            handleLoading(true)
            try {
                initCommunityDetail()
            } catch (e: Throwable) {
                showToast("데이터를 불러오는데 실패했습니다.")
                Timber.e(": $e")
            }
            handleLoading(false)
        }
    }

    private suspend fun initCommunityDetail() {
        user = dataRepository.getUser(SocialInfo.id)
        val community = communityDetail
        _community.postValue(community)
        _comment.postValue(community.comment)
    }

    fun makeComment() {
        viewModelScope.launch(dispatcherProvider.io) {
            handleLoading(true)
            val time = dateUtil.getCurrentDateFromStringFormat()
            val setComment = dataRepository.setComment(
                SetCommentQuery(
                    user.company,
                    communityDetail.idx,
                    user.id,
                    user.name,
                    content.value!!,
                    time
                )
            ).result(Error().stackTrace)
            if (setComment) {
                val refresh = dataRepository.refreshCommunity(user.company).result(Throwable().stackTrace)
                if (refresh) {
                    val comment = communityDetail.comment.toMutableList()
                    comment.add((Comment(user.id, user.name, time, content.value!!)))
                    _comment.postValue(comment)
                    content.postValue("")
                }
            }
            handleLoading(false)
        }
    }
}