package com.mostafahelal.prayerapp.qibla.data.qiblaRepository

import android.util.Log
import com.mostafahelal.prayerapp.qibla.data.network.QiblaService
import com.mostafahelal.prayerapp.qibla.data.response.Data
import com.mostafahelal.prayerapp.qibla.domin.qiblaRepo.QiblaRepository
import com.mostafahelal.prayerapp.utils.States
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class QuiblaRepositoryImp @Inject constructor(
    private val api: QiblaService
) : QiblaRepository {

    companion object {
        private const val TAG = "QiblaRepository"
    }
    override suspend fun getQiblaDirections(
        lat: Double,
        long: Double
    )=  flow<States<Data>> {
        emit(States.loading())
        val response = api.getQiblaDirection(lat, long)
        response.data?.let {
            if (response.status=="Ok"){
                emit(States.success(it))
            }

        }
    }.catch {
        Log.d(QuiblaRepositoryImp.TAG, "getQibla: failed = ${it.message}")
        emit(States.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

}