package com.mostafahelal.prayerapp.prayer.data.remote

import android.util.Log
import com.mostafahelal.prayerapp.prayer.domin.model.Schedule
import com.mostafahelal.prayerapp.prayer.domin.model.toSchedule
import com.mostafahelal.prayerapp.prayer.data.remote.network.PrayerService
import com.mostafahelal.prayerapp.utils.States
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val prayerService: PrayerService
){
    companion object {
        internal const val TAG = "RemoteDataSource"
    }

    suspend fun getSchedule(lat: Double, long: Double, month: Int, year: Int) =
        flow<States<List<Schedule>>> {
            emit(States.loading())
            val response = prayerService.getSchedule(lat, long, month, year)
            response.data.let {
                if (it?.isNotEmpty() == true) emit(States.success(it.toSchedule()))
                else emit(States.success(listOf()))
            }
        }.catch {
            Log.d(TAG, "getSchedule: failed = ${it.message}")
            emit(States.failed(it.message ?: ""))
        }.flowOn(Dispatchers.IO)

}