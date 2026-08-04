package com.dlancers.api_data
import com.dlancers.api_data.presentation.viewmodel.ProductViewModel
import com.dlancers.api_data.presentation.screen.ProductScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
@AndroidEntryPoint
class MyActivity: ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val productViewModel: ProductViewModel =hiltViewModel()
            ProductScreen(
                viewModel =productViewModel
            )
            }
        }
    }


// --> Build Flavours