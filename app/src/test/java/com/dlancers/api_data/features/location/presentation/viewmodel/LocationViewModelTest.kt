package com.dlancers.api_data.features.location.presentation.viewmodel

import com.dlancers.api_data.features.location.domain.model.Location
import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import com.dlancers.api_data.features.location.domain.repository.LocationRepository
import com.dlancers.api_data.features.location.domain.usecase.GetCurrentLocationUseCase
import com.dlancers.api_data.features.location.presentation.state.LocationPrecision
import com.dlancers.api_data.features.location.presentation.state.LocationState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocationViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onGetMyLocationWithPermission_emitsSuccessState_whenUseCaseSucceeds() = runTest {
        val location = Location(latitude = 51.5, longitude = -0.12)
        val viewModel = createViewModel(
            result = LocationFetchResult.Success(location),
        )

        viewModel.onGetMyLocationWithPermission(LocationPrecision.Precise)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LocationState.Success)
        val successState = state as LocationState.Success
        assertEquals(location, successState.location)
        assertEquals(LocationPrecision.Precise, successState.precision)
    }

    @Test
    fun onGetMyLocationWithPermission_emitsErrorState_whenUseCaseFails() = runTest {
        val viewModel = createViewModel(
            result = LocationFetchResult.Failure("Location unavailable"),
        )

        viewModel.onGetMyLocationWithPermission(LocationPrecision.Approximate)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LocationState.Error)
        assertEquals(
            "Location unavailable",
            (state as LocationState.Error).message,
        )
    }

    @Test
    fun onLocationPermissionDenied_emitsPermissionDeniedState() = runTest {
        val viewModel = createViewModel(
            result = LocationFetchResult.Failure("Should not be called"),
        )

        viewModel.onLocationPermissionDenied(isPermanentlyDenied = true)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is LocationState.PermissionDenied)
        assertTrue((state as LocationState.PermissionDenied).isPermanentlyDenied)
    }

    private fun createViewModel(result: LocationFetchResult): LocationViewModel {
        val repository = FakeLocationRepository(result)
        val useCase = GetCurrentLocationUseCase(repository)
        return LocationViewModel(useCase)
    }

    private class FakeLocationRepository(
        private val result: LocationFetchResult,
    ) : LocationRepository {

        override suspend fun getCurrentLocation(): LocationFetchResult {
            return result
        }
    }
}
