package com.mostafahelal.prayerapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.mostafahelal.prayerapp.components.Screen
import com.mostafahelal.prayerapp.prayer.presentation.HomePage
import com.mostafahelal.prayerapp.prayer.presentation.viewmodels.HomeViewModel
import com.mostafahelal.prayerapp.qibla.presentation.QiblaScreen
import com.mostafahelal.prayerapp.qibla.presentation.viewmodel.QiblaViewModel
import com.mostafahelal.prayerapp.ui.theme.PrayerTheme
import com.mostafahelal.prayerapp.utils.LocationUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val qiblaViewModel: QiblaViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private var isPermissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestLocationPermission()

        setContent {
            PrayerTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomePage(
                            calendar = homeViewModel.currentSchedule.hijriDate,
                            timingSchedule = homeViewModel.timingSchedule,
                            goToPreviousDay = {},
                            goToNextDay = {},
                            timeNextPray = homeViewModel.nextPray,
                            descNextPray = homeViewModel.descNextPray,
                            locationAddress = homeViewModel.locationAddress,
                            getInterval = homeViewModel::getIntervalText,
                            onShowQiblaDirection = {
                                currentLocation?.let { location ->
                                    navController.navigate(Screen.Qibla.createRoute(location.latitude, location.longitude))
                                }
                            }
                        )
                    }
                    composable(
                        Screen.Qibla.route,
                        arguments = Screen.Qibla.navArguments
                    ) { backStackEntry ->
                        val latitude = backStackEntry.arguments?.getString("latitude")?.toDoubleOrNull()
                        val longitude = backStackEntry.arguments?.getString("longitude")?.toDoubleOrNull()

                        // Log the received latitude and longitude
                        Log.d("TAG", "Received Qibla Coordinates: Latitude = $latitude, Longitude = $longitude")

                        if (latitude != null && longitude != null) {
                            QiblaScreen(
                                latitude = latitude,
                                longitude = longitude,
                                qiblaDirection = qiblaViewModel.qiblaDirection
                            )
                        } else {
                            Log.e("TAG", "Error: Latitude or Longitude is null in QiblaScreen")
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            isPermissionGranted = permissions.values.all { it }
            if (isPermissionGranted) {
                fetchLocation()
            } else {
                // Handle permission denied scenario
                Log.e("TAG", "Location permission denied")
            }
        }
        LocationUtils.launchPermission(locationPermissionRequest)
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        if (isPermissionGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    currentLocation = it
                    homeViewModel.getLocationAddress(this, it)
                    homeViewModel.getPrayerSchedule(
                        it.latitude,
                        it.longitude,
                        Timestamp.now()
                    )
                } ?: Log.e("TAG", "Location is null when fetching")
            }
        }
    }
}

