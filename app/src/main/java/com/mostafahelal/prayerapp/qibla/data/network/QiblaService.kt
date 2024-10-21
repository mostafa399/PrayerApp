package com.mostafahelal.prayerapp.qibla.data.network

import com.mostafahelal.prayerapp.qibla.data.response.QiblaResponse
import retrofit2.http.GET
import retrofit2.http.Path
interface QiblaService {
    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaDirection(
        @Path("latitude") lat: Double,
        @Path("longitude") long: Double,
    ): QiblaResponse
}
