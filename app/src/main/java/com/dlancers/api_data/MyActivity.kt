package com.dlancers.api_data

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dlancers.api_data.core.presentation.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MainScreen()
        }
    }
}