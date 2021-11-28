package com.bottotop.asset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bottotop.core.base.BaseViewModel
import com.bottotop.core.di.DispatcherProvider
import com.bottotop.model.Schedule
import com.bottotop.model.User
import com.bottotop.model.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AssetViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val dataRepository: DataRepository
) : BaseViewModel("자원뷰모델") {

    private var _schedules = MutableLiveData<List<Pair<String, List<Pair<String, Schedule>>>>>()
    val schedules = _schedules

    init {
        handleLoading(true)
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                initPayData()
            }catch (e : Throwable){
                showToast("데이터를 불러오는데 실패했습니다.")
                Timber.e(":에러 $e" )
            } finally {
                handleLoading(false)
            }
        }
    }

    private suspend fun initPayData(){
        val member = dataRepository.getMembers()
        val test = dataRepository.getScheduleAll(
            mapOf(Pair("com_id", member[0].company))
        )
        if(test.isSuccess){
            val schedules = getPay(member , test.getOrNull()!!)
            _schedules.postValue(schedules)
        }
    }

    private fun getPay(member : List<User>, schedules : List<Schedule>) : List<Pair<String, List<Pair<String, Schedule>>>> {
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