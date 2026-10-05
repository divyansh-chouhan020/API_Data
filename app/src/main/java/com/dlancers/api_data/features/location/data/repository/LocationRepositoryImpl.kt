package com.dlancers.api_data.features.location.data.repository

import com.dlancers.api_data.features.location.data.datasource.LocationDataSource
import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import com.dlancers.api_data.features.location.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource,
) : LocationRepository {

    override suspend fun getCurrentLocation(): LocationFetchResult {
        return locationDataSource.getCurrentLocation()
    }
}
