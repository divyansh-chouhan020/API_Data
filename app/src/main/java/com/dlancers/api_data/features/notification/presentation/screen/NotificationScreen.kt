package com.dlancers.api_data.features.notification.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dlancers.api_data.features.notification.presentation.state.NotificationState
import com.dlancers.api_data.features.notification.presentation.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hasRequestedPermission  by rememberSaveable { mutableStateOf(false) }
    var showRationaleDialog by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            viewModel.onPermissionAvailable()
        } else {
            val activity = context as? Activity
            val isPermanentlyDenied = hasRequestedPermission &&
                    activity != null &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,

                        Manifest.permission.POST_NOTIFICATIONS,
                    )

            viewModel.onNotificationPermissionDenied(isPermanentlyDenied = isPermanentlyDenied)
        }
    }

    val currentState = state

    if (showRationaleDialog) {
        NotificationRationaleDialog(
            onContinue = {
                showRationaleDialog = false

                hasRequestedPermission = true

                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            onDismiss = {
                showRationaleDialog = false
            },
        )
    }

    when (currentState) {
        is NotificationState.PermissionDenied -> {
            NotificationUnavailableDialog(
                message = if (currentState.isPermanentlyDenied) {

                    "Notification permission was denied. Enable it in app settings to receive alerts."
                } else {
                    "Notification permission is required to receive alerts."

                },
                showOpenSettings = currentState.isPermanentlyDenied,
                onDismiss = { viewModel.onPermissionDeniedDismissed() },
                onOpenSettings = {
                    openAppSettings(context)

                    viewModel.onPermissionDeniedDismissed()
                },
            )
        }

        is NotificationState.Disabled -> {
            NotificationUnavailableDialog(
                message = "Notifications are turned off for this app. Enable them in settings to receive alerts.",
                showOpenSettings = true,
                onDismiss = { viewModel.onDisabledDismissed() },
                onOpenSettings = {
                    openAppSettings(context)
                    viewModel.onDisabledDismissed()
                },
            )
        }

        is NotificationState.Error -> {
            NotificationUnavailableDialog(
                message = currentState.message,
                showOpenSettings = false,
                onDismiss = { viewModel.onErrorDismissed() },
                onOpenSettings = {},
            )
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                EnableAlertsContent(
                    isChecking = currentState is NotificationState.Checking,
                    onEnableAlertsClick = {
                        viewModel.prepareForNewAlertsRequest()

                        val needsRuntimePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS,
                                ) != android.content.pm.PackageManager.PERMISSION_GRANTED

                        if (needsRuntimePermission) {
                            showRationaleDialog = true
                        } else {
                            viewModel.onPermissionAvailable()
                        }
                    },
                )

                if (currentState is NotificationState.Shown) {
                    LaunchedEffect(currentState) {
                        Toast.makeText(context, "Alerts enabled", Toast.LENGTH_SHORT).show()
                    }
                    LaunchedEffect(currentState) {
                        viewModel.onShownDismissed()
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationRationaleDialog(
    onContinue: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Turn on Alerts") },
        text = {
            Text(
                text = "We will send you order updates and let you know about deals you might like,  " +
                        "You can turn this off anytime in settings.",
                textAlign = TextAlign.Start,
            )
        },
        confirmButton = {
            TextButton(onClick = onContinue) {
                Text(text = "Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Not now")
            }
        },
    )
}

@Composable
private fun EnableAlertsContent(
    isChecking: Boolean,
    onEnableAlertsClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp),
        ) {
            Button(
                onClick = onEnableAlertsClick,
                enabled = !isChecking,
            ) {
                Text(text = "Enable alerts")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Get notified about order updates and deals.",
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun NotificationUnavailableDialog(
    message: String,
    showOpenSettings: Boolean,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Alerts Unavailable") },
        text = { Text(text = message) },
        confirmButton = {
            if (showOpenSettings) {
                TextButton(onClick = onOpenSettings) {
                    Text(text = "Open Settings")
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text(text = "OK")
                }
            }
        },
        dismissButton = {
            if (showOpenSettings) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        },
    )
}

private fun openAppSettings(context: android.content.Context) {
    val settingsIntent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null),
    )
    context.startActivity(settingsIntent)
}