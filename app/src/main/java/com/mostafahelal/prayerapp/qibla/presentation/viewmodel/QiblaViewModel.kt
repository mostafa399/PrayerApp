package com.mostafahelal.prayerapp.qibla.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mostafahelal.prayerapp.qibla.domin.qiblaRepo.QiblaRepository
import com.mostafahelal.prayerapp.utils.States
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class QiblaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: QiblaRepository,
) : ViewModel() {

    private val latitude: String? = savedStateHandle["latitude"]
    private val longitude: String? = savedStateHandle["longitude"]

    var qiblaDirection by mutableStateOf<Double?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        if (latitude != null && longitude != null) {
            Log.d("TAG", "Latitude: $latitude, Longitude: $longitude")
            getQiblaDirection(latitude.toDouble(), longitude.toDouble())
        } else {
            Log.e("TAG", "Latitude or Longitude is null")
        }
    }

    private fun getQiblaDirection(lat: Double, long: Double) {
        viewModelScope.launch {
            repository.getQiblaDirections(lat, long).collect { state ->
                when (state) {
                    is States.Loading -> isLoading = true
                    is States.Success -> {
                        qiblaDirection = state.data.direction
                        isLoading = false
                    }
                    is States.Failed -> {
                        errorMessage = state.message
                        isLoading = false
                    }
                }
            }
        }
    }
}
