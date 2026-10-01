package com.dlancers.api_data.features.product.presentation.viewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.dlancers.api_data.features.product.presentation.state.ProductState
import com.dlancers.api_data.features.product.domain.repository.ProductRepository
import kotlinx.coroutines.delay

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel(){

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
                      val products = productRepository.getProducts()

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

