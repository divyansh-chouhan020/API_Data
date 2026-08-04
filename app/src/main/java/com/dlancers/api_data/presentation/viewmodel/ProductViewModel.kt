package com.dlancers.api_data.presentation.viewmodel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlancers.api_data.domain.repository.ProductRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.dlancers.api_data.presentation.state.ProductState // FIX: full package import for ProductState
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
): ViewModel(){

    private val _productState = MutableStateFlow<ProductState> ( ProductState.Loading)


    val productState :StateFlow<ProductState> =  _productState.asStateFlow()

    // As soon as the app opens the init function must be invoked and calls the getProduct()

    init {
        getProducts()
    }

    private fun getProducts()
    {
        viewModelScope.launch{
            try {
                      val products = productRepository.getProducts()
                     _productState.value = ProductState.Success( products );

            }catch ( exception : Exception){

                       _productState.value = ProductState.Failure( exception.message ?: "Unexpected Error");

            }
        }
    }
}