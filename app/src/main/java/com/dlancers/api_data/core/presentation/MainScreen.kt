package com.dlancers.api_data.core.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dlancers.api_data.features.product.presentation.screen.ProductScreen
import com.dlancers.api_data.features.product.presentation.viewmodel.ProductViewModel

@Composable
fun MainScreen() {
    val productViewModel: ProductViewModel = hiltViewModel()
    ProductScreen(viewModel = productViewModel)
}
