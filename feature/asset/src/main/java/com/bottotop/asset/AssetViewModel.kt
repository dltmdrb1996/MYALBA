package com.bottotop.asset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.core.global.SocialInfo
import com.bottotop.model.Schedule
import com.bottotop.model.ScheduleInfo
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections.synchronizedList
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("자원뷰모델") {

    private var result = mutableListOf<Pair<String,MutableList<Schedule>>>()


    private var _schedules = MutableLiveData<List<Pair<String, List<Pair<String, Schedule>>>>>()
    val schedules = _schedules
    init {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val member = dataRepository.getMembers()
                val test = dataRepository.getScheduleAll(
                    mapOf(Pair("com_id", member[0].company))
                )
                if(test.isSuccess){
                    val schedules = getPay(member , test.getOrNull()!!)
                    _schedules.postValue(schedules)
                }
            }catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Log.e(TAG, ":에러 ${e}" )
            } finally {
                handleLoading(false)
            }
        }
    }

    suspend fun getPay(member : List<User> , schedules : List<Schedule>) : List<Pair<String, List<Pair<String, Schedule>>>> {
        return schedules.map { schedule ->
            var name = ""
            member.forEach { user ->
                if (user.id == schedule.id) {
                    name = user.name
                    return@forEach
                }
            }
            Pair(name , schedule)
        }.groupBy { it.first }.toList()
    }
}