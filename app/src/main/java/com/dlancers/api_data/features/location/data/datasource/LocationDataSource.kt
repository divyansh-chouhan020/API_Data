package com.dlancers.api_data.features.location.data.datasource

import com.dlancers.api_data.features.location.domain.model.LocationFetchResult

interface LocationDataSource {

    suspend fun getCurrentLocation(): LocationFetchResult
}
