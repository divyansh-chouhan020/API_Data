package com.dlancers.api_data.features.location.domain.model

sealed class LocationFetchResult {
    data class Success(val location: Location) : LocationFetchResult()

    data class Failure(val message: String) : LocationFetchResult()
}
