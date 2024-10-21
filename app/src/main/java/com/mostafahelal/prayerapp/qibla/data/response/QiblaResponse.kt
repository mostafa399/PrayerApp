package com.mostafahelal.prayerapp.qibla.data.response

import com.google.gson.annotations.SerializedName

data class QiblaResponse(
	@SerializedName("code")val code: Int? = null,
	@SerializedName("data")val data: Data? = null,
	@SerializedName("status")val status: String? = null
)

data class Data(
@SerializedName("latitude")val latitude: Double? = null,
@SerializedName("longitude")val longitude: Double? = null,
@SerializedName("direction")val direction: Double? = null
)
