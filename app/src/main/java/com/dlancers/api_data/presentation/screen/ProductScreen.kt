package com.dlancers.api_data.presentation.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dlancers.api_data.presentation.viewmodel.ProductViewModel
import com.dlancers.api_data.presentation.components.ProductCard
import com.dlancers.api_data.presentation.state.ProductState


@Composable
fun ProductScreen(
    viewModel: ProductViewModel
) {
    val state by viewModel.productState.collectAsState()

    when (val currentState = state) {

        is ProductState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ProductState.Success -> {

            val products = currentState.products

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = products,
                    key = { product -> product.id ?: product.hashCode() }
                ) {
                    product -> ProductCard(
                        product = product,
                        onFavouriteClick = {}
                    )
                }
            }

        }

        is ProductState.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message
                )
            }
        }

    }

}