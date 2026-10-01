package com.dlancers.api_data.features.location.domain.repository

import com.dlancers.api_data.features.location.domain.model.LocationFetchResult

interface LocationRepository {

    suspend fun getCurrentLocation(): LocationFetchResult
}
