package com.bottotop.member

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel("맴버뷰모델") {

    private val _members = MutableLiveData<List<MemberModel>>()
    val members = _members

    private val red = listOf(R.color.red_bg,R.color.red_light_font,R.color.red_deep_font)
    private val blue = listOf(R.color.blue_bg,R.color.blue_light_font,R.color.blue_deep_font)
    private val yellow = listOf(R.color.yellow_bg,R.color.yellow_light_font,R.color.yellow_deep_font)
    private val green = listOf(R.color.green_bg,R.color.green_light_font,R.color.green_deep_font)
    private val list = listOf(blue,yellow,red,green)

    init {
        viewModelScope.launch(dispatcherProvider.io){
            try {
                initData()
            } catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Timber.e("룸불러오기 에러 : e")
            }
        }
    }

    private suspend fun initData(){
        val member = dataRepository.getMembers()
        val companies = dataRepository.getCompanies()
        val membersModel = member.mapIndexed { index, user ->
            val idx = index%4
            MemberModel(user.id ,user.name , user.workOn ,user.birth ,
                user.tel , user.email , companies[index].pay , list[idx] ,
                companies[index].position)
        }
        _members.postValue(membersModel)
    }
}