package com.dlancers.api_data.features.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.dlancers.api_data.features.notification.domain.model.NotificationResult
import com.dlancers.api_data.features.notification.domain.usecase.ShowDemoNotificationUseCase
import com.dlancers.api_data.features.notification.presentation.state.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val showDemoNotificationUseCase: ShowDemoNotificationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<NotificationState>(NotificationState.Idle)
    val state: StateFlow<NotificationState> = _state.asStateFlow()


    fun onPermissionAvailable() {
        if (_state.value is NotificationState.Checking) return

        _state.value = NotificationState.Checking

        when (val result = showDemoNotificationUseCase()) {
            is NotificationResult.Shown -> {
                _state.value = NotificationState.Shown
            }

            is NotificationResult.Disabled -> {
                _state.value = NotificationState.Disabled
            }

            is NotificationResult.Failure -> {
                _state.value = NotificationState.Error(result.message)
            }
        }
    }

    fun onNotificationPermissionDenied(isPermanentlyDenied: Boolean) {
        _state.value = NotificationState.PermissionDenied(
            isPermanentlyDenied = isPermanentlyDenied,
        )
    }

    fun onShownDismissed() {
        if (_state.value is NotificationState.Shown) {
             _state.value = NotificationState.Idle
        }
    }

    fun onDisabledDismissed() {
        if (_state.value is NotificationState.Disabled) {
            _state.value = NotificationState.Idle
        }
    }

    fun onPermissionDeniedDismissed() {
        if (_state.value is NotificationState.PermissionDenied) {
            _state.value = NotificationState.Idle
        }
    }

    fun onErrorDismissed() {
        if (_state.value is NotificationState.Error) {
            _state.value = NotificationState.Idle
        }
    }

    fun prepareForNewAlertsRequest() {
        when (_state.value) {
            is NotificationState.Shown,
                 is NotificationState.Disabled,
             is NotificationState.PermissionDenied,
            is NotificationState.Error,
                -> _state.value = NotificationState.Idle

            else -> Unit
        }
    }
}