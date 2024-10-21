package com.mostafahelal.prayerapp.qibla.domin.qiblaRepo

import com.mostafahelal.prayerapp.prayer.domin.model.Schedule
import com.mostafahelal.prayerapp.qibla.data.response.Data
import com.mostafahelal.prayerapp.qibla.data.response.QiblaResponse
import com.mostafahelal.prayerapp.utils.States
import kotlinx.coroutines.flow.Flow

interface QiblaRepository {
    suspend fun getQiblaDirections(lat: Double, long: Double): Flow<States<Data>>

}