package com.dlancers.api_data.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dlancers.api_data.features.notification.presentation.screen.NotificationScreen
import com.dlancers.api_data.features.notification.presentation.viewmodel.NotificationViewModel
import com.dlancers.api_data.features.product.presentation.screen.ProductScreen
import com.dlancers.api_data.features.product.presentation.viewmodel.ProductViewModel

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Products",
                        )
                    },
                    label = { Text(text = "Products") },
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Alerts",
                        )
                    },
                    label = { Text(text = "Alerts") },
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> {
                    val productViewModel: ProductViewModel = hiltViewModel()
                    ProductScreen(viewModel = productViewModel)
                }

                1 -> {
                    val notificationViewModel: NotificationViewModel = hiltViewModel()
                    NotificationScreen(viewModel = notificationViewModel)
                }
            }
        }
    }
}
