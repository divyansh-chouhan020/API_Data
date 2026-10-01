package com.dlancers.api_data.features.location.data.repository

import com.dlancers.api_data.features.location.data.datasource.LocationDataSource
import com.dlancers.api_data.features.location.domain.model.Location
import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationRepositoryImplTest {

    @Test
    fun getCurrentLocation_returnsSuccess_whenDataSourceSucceeds() = runTest {
        val expectedLocation = Location(latitude = 10.0, longitude = 20.0)
        val dataSource = FakeLocationDataSource(
            result = LocationFetchResult.Success(expectedLocation),
        )
        val repository = LocationRepositoryImpl(dataSource)

        val result = repository.getCurrentLocation()

        assertTrue(result is LocationFetchResult.Success)
        assertEquals(expectedLocation, (result as LocationFetchResult.Success).location)
    }

    @Test
    fun getCurrentLocation_returnsFailure_whenDataSourceFails() = runTest {
        val dataSource = FakeLocationDataSource(
            result = LocationFetchResult.Failure("Provider error"),
        )
        val repository = LocationRepositoryImpl(dataSource)

        val result = repository.getCurrentLocation()

        assertTrue(result is LocationFetchResult.Failure)
        assertEquals("Provider error", (result as LocationFetchResult.Failure).message)
    }

    private class FakeLocationDataSource(
        private val result: LocationFetchResult,
    ) : LocationDataSource {

        override suspend fun getCurrentLocation(): LocationFetchResult {
            return result
        }
    }
}
