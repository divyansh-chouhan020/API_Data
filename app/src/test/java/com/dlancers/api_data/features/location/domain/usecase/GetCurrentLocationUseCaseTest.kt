package com.dlancers.api_data.features.location.domain.usecase

import com.dlancers.api_data.features.location.domain.model.Location
import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import com.dlancers.api_data.features.location.domain.repository.LocationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCurrentLocationUseCaseTest {

    @Test
    fun invoke_returnsSuccess_whenRepositoryReturnsLocation() = runTest {
        val expectedLocation = Location(latitude = 12.34, longitude = 56.78)
        val repository = FakeLocationRepository(
            result = LocationFetchResult.Success(expectedLocation),
        )
        val useCase = GetCurrentLocationUseCase(repository)

        val result = useCase()

        assertTrue(result is LocationFetchResult.Success)
        val successResult = result as LocationFetchResult.Success
        assertEquals(expectedLocation, successResult.location)
    }

    @Test
    fun invoke_returnsFailure_whenRepositoryFails() = runTest {
        val repository = FakeLocationRepository(
            result = LocationFetchResult.Failure("Location unavailable"),
        )
        val useCase = GetCurrentLocationUseCase(repository)

        val result = useCase()

        assertTrue(result is LocationFetchResult.Failure)
        assertEquals(
            "Location unavailable",
            (result as LocationFetchResult.Failure).message,
        )
    }

    private class FakeLocationRepository(
        private val result: LocationFetchResult,
    ) : LocationRepository {

        override suspend fun getCurrentLocation(): LocationFetchResult {
            return result
        }
    }
}
