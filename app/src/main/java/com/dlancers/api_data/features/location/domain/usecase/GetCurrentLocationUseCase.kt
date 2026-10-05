package com.dlancers.api_data.features.location.domain.usecase

import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import com.dlancers.api_data.features.location.domain.repository.LocationRepository

class GetCurrentLocationUseCase constructor(
    private val locationRepository: LocationRepository,
) {

    suspend operator fun invoke(): LocationFetchResult {
        return locationRepository.getCurrentLocation()
    }
}
