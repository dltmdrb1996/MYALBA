package com.bottotop.member

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel("맴버뷰모델") {

    private val _members = MutableLiveData<List<MemberModel>>()
    val members = _members

    val red = listOf<Int>(R.color.red_bg,R.color.red_light_font,R.color.red_deep_font)
    val blue = listOf<Int>(R.color.blue_bg,R.color.blue_light_font,R.color.blue_deep_font)
    val yellow = listOf<Int>(R.color.yellow_bg,R.color.yellow_light_font,R.color.yellow_deep_font)
    val green = listOf<Int>(R.color.green_bg,R.color.green_light_font,R.color.green_deep_font)
    val list = listOf(blue,yellow,red,green)

    init {
        viewModelScope.launch(dispatcherProvider.io){
            try {
                val member = dataRepository.getMembers()
                val companies = dataRepository.getCompanies()
                val membersModel = member.mapIndexed { index, user ->
                    val idx = index%4
                    MemberModel(user.id ,user.name , user.workOn ,user.birth ,
                        user.tel , user.email , companies[index].pay , list[idx] ,
                        companies[index].position)
                }
                _members.postValue(membersModel)
            } catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Log.e(TAG, "룸불러오기 에러 : e", )
            }
        }
    }


}