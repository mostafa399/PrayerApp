package com.mostafahelal.prayerapp.qibla.presentation
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mostafahelal.prayerapp.R
import com.mostafahelal.prayerapp.qibla.data.response.Data

@Composable
fun QiblaScreen(
    latitude:Double,
    longitude:Double,
    qiblaDirection: Double?
) {
    val cameraPositionState = rememberCameraPositionState()

    // Move camera to user location when available
    LaunchedEffect(qiblaDirection) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(latitude, longitude), 15f
            )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
        ) {
                Marker(
                    state = MarkerState(position = LatLng(latitude,longitude)),
                    title = "Your Location"
                )

        }

        qiblaDirection?.let { directionData ->
            Image(
                painter = painterResource(id = R.drawable.qibla),
                contentDescription = "Qibla Arrow",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        rotationZ = directionData.toFloat()
                    }
            )
        }
    }
}
