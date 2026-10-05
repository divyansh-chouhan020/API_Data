package com.dlancers.api_data.features.location.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationMapView(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
) {
    val mapPosition = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapPosition, DEFAULT_MAP_ZOOM)
    }

    LaunchedEffect(latitude, longitude) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(mapPosition, DEFAULT_MAP_ZOOM),
            durationMs = MAP_CAMERA_ANIMATION_MS,
        )
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            Marker(
                state = MarkerState(position = mapPosition),
                title = "You are here",
            )
        }
    }
}

private const val DEFAULT_MAP_ZOOM = 15f
private const val MAP_CAMERA_ANIMATION_MS = 500
