package com.dlancers.api_data.features.product.domain.repository
import com.dlancers.api_data.features.product.domain.models.Product
interface ProductRepository {

    suspend  fun getProducts(): List<Product>

}