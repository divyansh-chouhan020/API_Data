package com.dlancers.api_data.features.location.presentation.state

import com.dlancers.api_data.features.location.domain.model.Location

enum class LocationPrecision {
    Approximate,
    Precise,
}

sealed class LocationState {

    data object Idle : LocationState()

    data object Loading : LocationState()

    data class Success(
        val location: Location,
        val precision: LocationPrecision,
    ) : LocationState()

    data class PermissionDenied(
        val isPermanentlyDenied: Boolean,
    ) : LocationState()

    data class Error(
        val message: String,
    ) : LocationState()
}
