package com.dlancers.api_data.features.location.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dlancers.api_data.features.location.presentation.state.LocationPrecision
import com.dlancers.api_data.features.location.presentation.state.LocationState
import com.dlancers.api_data.features.location.presentation.viewmodel.LocationViewModel

private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
)
@Composable
fun LocationScreen(
    viewModel: LocationViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var hasRequestedPermission by rememberSaveable { mutableStateOf(false) }
    var showSystemPermissionPrompt by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissionResults ->
        showSystemPermissionPrompt = false

        val coarseGranted = permissionResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val fineGranted = permissionResults[Manifest.permission.ACCESS_FINE_LOCATION] == true

        if (coarseGranted || fineGranted) {
            viewModel.onLocationPermissionGranted(isPrecise = fineGranted)
        } else {
            val activity = context as? Activity
            val isPermanentlyDenied = hasRequestedPermission &&
                activity != null &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )

            viewModel.onLocationPermissionDenied(isPermanentlyDenied = isPermanentlyDenied)
        }
    }

    val currentState = state

    if (showSystemPermissionPrompt) {
        SystemLocationPermissionPromptDialog(
            onContinue = {
                hasRequestedPermission = true
                permissionLauncher.launch(locationPermissions)
            },
            onDismiss = {
                showSystemPermissionPrompt = false
            },
        )
    }

    when (currentState) {
        is LocationState.PermissionDenied -> {
            PermissionDeniedDialog(
                isPermanentlyDenied = currentState.isPermanentlyDenied,
                onDismiss = { viewModel.onPermissionDeniedDismissed() },
                onOpenSettings = {
                    val settingsIntent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null),
                    )
                    context.startActivity(settingsIntent)
                    viewModel.onPermissionDeniedDismissed()
                },
            )
        }

        is LocationState.Error -> {
            ErrorDialog(
                message = currentState.message,
                onDismiss = { viewModel.onErrorDismissed() },
            )
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LocationRequestContent(
                    isLoading = currentState is LocationState.Loading,
                    onGetMyLocationClick = {
                        viewModel.prepareForNewLocationRequest()

                        if (hasLocationPermission(context)) {
                            hasRequestedPermission = true
                            permissionLauncher.launch(locationPermissions)
                        } else {
                            showSystemPermissionPrompt = true
                        }
                    },
                )

                if (currentState is LocationState.Success) {
                    LaunchedEffect(currentState.location) {
                        Toast.makeText(context, "Location received", Toast.LENGTH_SHORT).show()
                    }

                    LocationReceivedDialog(
                        latitude = currentState.location.latitude,
                        longitude = currentState.location.longitude,
                        precision = currentState.precision,
                        onDismiss = { viewModel.onLocationResultDismissed() },
                    )
                }
            }
        }
    }
}

@Composable
private fun SystemLocationPermissionPromptDialog(
    onContinue: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Allow Location Access") },
        text = {
            Text(
                text = "On the next screen, Android will show the location permission bar.\n\n" +
                    "You can choose:\n" +
                    "• Only this time — permission is asked again later\n" +
                    "• While using the app — keep permission until you change it\n" +
                    "• Don't allow\n\n" +
                    "You can also choose Approximate or Precise location.",
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
                Text(text = "Cancel")
            }
        },
    )
}

@Composable
private fun LocationRequestContent(
    isLoading: Boolean,
    onGetMyLocationClick: () -> Unit,
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
                onClick = onGetMyLocationClick,
                enabled = !isLoading,
            ) {
                Text(text = "Get My Location")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tip: Choose \"Only this time\" on the Android permission bar " +
                    "if you want to be asked again next time.",
                textAlign = TextAlign.Center,
            )

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Fetching your location...")
            }
        }
    }
}

@Composable
private fun LocationReceivedDialog(
    latitude: Double,
    longitude: Double,
    precision: LocationPrecision,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Current Location") },
        text = {
            Column {
                Text(text = "Latitude: $latitude")
                Text(text = "Longitude: $longitude")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Accuracy: ${precision.name}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
    )
}

@Composable
private fun PermissionDeniedDialog(
    isPermanentlyDenied: Boolean,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Location Permission Required") },
        text = {
            Text(
                text = if (isPermanentlyDenied) {
                    "Location permission was denied. Please enable it in app settings to use this feature."
                } else {
                    "Location permission is required to get your current location."
                },
            )
        },
        confirmButton = {
            if (isPermanentlyDenied) {
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
            if (isPermanentlyDenied) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        },
    )
}
@Composable
private fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Location Error") },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
    )
}
@Composable
@Preview(showBackground = true)
private fun ErrorDialogPreview (){

    ErrorDialog(
        message= "No Location",
    onDismiss ={}
    )
}
private fun hasLocationPermission(context: Context): Boolean {
    val coarseGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED

    val fineGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED

    return coarseGranted || fineGranted
}

