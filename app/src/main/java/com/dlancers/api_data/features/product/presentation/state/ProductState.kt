package com.dlancers.api_data.features.product.presentation.state
import com.dlancers.api_data.features.product.domain.models.Product

// There are 3 states that are possible
//1. Success  2. Failure and 3. Loading
// Since we have only three states and these changes are dynamic like List<Product> on success or String messages on the error
// lets use the sealed class
sealed class ProductState{
    // if it is a Loading state
    data object Loading :ProductState()

    // On Success State
    data class Success (
        val products : List<Product>
    ):ProductState()

    //on Failure State
    data class Failure (
        val message : String
    ):ProductState()

}