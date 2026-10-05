package com.dlancers.api_data
import android.util.Log
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dlancers.api_data.core.presentation.MainScreen
import com.dlancers.api_data.ui.theme.API_DataTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyActivity : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)
        Log.d("Environment Check", "Flavour : ${BuildConfig.FLAVOR}")
        Log.d("Environment Check", "Build Type : ${BuildConfig.BUILD_TYPE}")
        enableEdgeToEdge()
        setContent {
            API_DataTheme {
                MainScreen()
            }
        }
    }
}
