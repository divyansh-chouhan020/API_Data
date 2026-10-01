package com.dlancers.api_data.features.location.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlancers.api_data.features.location.domain.model.LocationFetchResult
import com.dlancers.api_data.features.location.domain.usecase.GetCurrentLocationUseCase
import com.dlancers.api_data.features.location.presentation.state.LocationPrecision
import com.dlancers.api_data.features.location.presentation.state.LocationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<LocationState>(LocationState.Idle)
    val state: StateFlow<LocationState> = _state.asStateFlow()

    fun onLocationPermissionGranted(isPrecise: Boolean) {
        val precision = if (isPrecise) {
            LocationPrecision.Precise
        } else {
            LocationPrecision.Approximate
        }
        fetchCurrentLocation(precision)
    }

    fun onLocationPermissionDenied(isPermanentlyDenied: Boolean) {
        _state.value = LocationState.PermissionDenied(
            isPermanentlyDenied = isPermanentlyDenied,
        )
    }

    fun onGetMyLocationWithPermission(precision: LocationPrecision) {
        fetchCurrentLocation(precision)
    }

    fun onPermissionDeniedDismissed() {
        if (_state.value is LocationState.PermissionDenied) {
            _state.value = LocationState.Idle
        }
    }

    fun onErrorDismissed() {
        if (_state.value is LocationState.Error) {
            _state.value = LocationState.Idle
        }
    }

    fun onLocationResultDismissed() {
        if (_state.value is LocationState.Success) {
            _state.value = LocationState.Idle
        }
    }

    fun prepareForNewLocationRequest() {
        when (_state.value) {
            is LocationState.Success,
            is LocationState.Error,
            -> _state.value = LocationState.Idle

            else -> Unit
        }
    }

    private fun fetchCurrentLocation(precision: LocationPrecision) {
        viewModelScope.launch {
            _state.value = LocationState.Loading

            when (val result = getCurrentLocationUseCase()) {
                is LocationFetchResult.Success -> {
                    _state.value = LocationState.Success(
                        location = result.location,
                        precision = precision,
                    )
                }

                is LocationFetchResult.Failure -> {
                    _state.value = LocationState.Error(
                        message = result.message,
                    )
                }
            }
        }
    }
}
