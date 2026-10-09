package com.dlancers.api_data.features.product.presentation.viewmodel
import com.dlancers.api_data.features.product.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.dlancers.api_data.features.product.presentation.state.ProductState
import com.dlancers.api_data.features.product.domain.usecase.GetProductsUseCase
import kotlinx.coroutines.delay
import com.dlancers.api_data.features.product.domain.usecase.ObserveFavouriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
    private val observeFavouriteUseCase: ObserveFavouriteUseCase

): ViewModel(){

    fun toggleFavourite(uid: String) {
        viewModelScope.launch {
            toggleFavouriteUseCase(uid)
        }
    }

    val favouriteUids = observeFavouriteUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )
    // Minimal Loading time
    val minimumLoadingTime = 1000L

    private val _productState = MutableStateFlow<ProductState> ( ProductState.Loading)


    val productState :StateFlow<ProductState> =  _productState.asStateFlow()

    // As soon as the app opens the init function must be invoked and calls the getProduct()

    init {
        getProducts()
    }

    private fun getProducts()
    {
        viewModelScope.launch{

            val loadingStartTime = System.currentTimeMillis()

            try {
                      val products = getProductsUseCase()

                      val timeElapsed = System.currentTimeMillis() - loadingStartTime

                      val remainingTime = minimumLoadingTime - timeElapsed

                       if (remainingTime>0)
                       {
                           delay(remainingTime)
                       }

                     _productState.value = ProductState.Success( products );

            }catch ( exception : Exception){

                     val timeElapsed = System.currentTimeMillis() - loadingStartTime

                      val remainingTime = minimumLoadingTime - timeElapsed

                        if (remainingTime >0)
                        {
                            delay(remainingTime)
                        }

                       _productState.value = ProductState.Failure( exception.message ?: "Unexpected Error");

            }
        }
    }
}

