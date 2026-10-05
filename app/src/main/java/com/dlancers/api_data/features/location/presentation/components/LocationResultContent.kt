package com.dlancers.api_data.features.location.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dlancers.api_data.features.location.presentation.state.LocationPrecision

@Composable
fun LocationResultContent(
    latitude: Double,
    longitude: Double,
    precision: LocationPrecision,
    onRefreshLocation: () -> Unit,
    isRefreshing: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        LocationCoordinatesPanel(
            latitude = latitude,
            longitude = longitude,
            precision = precision,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            LocationMapView(
                latitude = latitude,
                longitude = longitude,
                modifier = Modifier.fillMaxSize(),
            )

            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRefreshLocation,
            enabled = !isRefreshing,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Refresh Location")
        }
    }
}
