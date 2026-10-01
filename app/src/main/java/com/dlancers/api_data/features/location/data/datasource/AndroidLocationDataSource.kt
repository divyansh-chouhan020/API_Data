package com.dlancers.api_data.features.location.data.datasource

import android.annotation.SuppressLint
import com.dlancers.api_data.features.location.domain.model.Location
import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AndroidLocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationFetchResult {
        return try {
            withTimeout(LOCATION_TIMEOUT_MILLIS) {
                fetchCurrentLocationWithFallback()
            }
        } catch (exception: TimeoutCancellationException) {
            LocationFetchResult.Failure(LOCATION_TIMEOUT_MESSAGE)
        } catch (exception: Exception) {
            LocationFetchResult.Failure(
                exception.message ?: LOCATION_RETRIEVAL_FAILED_MESSAGE,
            )
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun fetchCurrentLocationWithFallback(): LocationFetchResult {
        val cancellationTokenSource = CancellationTokenSource()

         val currentLocationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setMaxUpdateAgeMillis(MAX_LOCATION_AGE_MILLIS)
            .build()

        val currentLocation = fusedLocationProviderClient.getCurrentLocation(
            currentLocationRequest,
            cancellationTokenSource.token,
        ).await()

        val resolvedLocation = currentLocation ?: fusedLocationProviderClient.lastLocation.await()

        return if (resolvedLocation == null) {
            LocationFetchResult.Failure(LOCATION_UNAVAILABLE_MESSAGE)
        } else {
            LocationFetchResult.Success(
                Location(
                    latitude = resolvedLocation.latitude,
                    longitude = resolvedLocation.longitude,
                ),
            )
        }
    }

    private companion object {
        const val MAX_LOCATION_AGE_MILLIS = 60_000L
        const val LOCATION_TIMEOUT_MILLIS = 15_000L
        const val LOCATION_UNAVAILABLE_MESSAGE =
            "Location is currently unavailable. Please enable device location and try again."
        const val LOCATION_TIMEOUT_MESSAGE =
            "Could not get your location in time. Please ensure device location is turned on and try again."
        const val LOCATION_RETRIEVAL_FAILED_MESSAGE = "Failed to retrieve location"
    }
}
