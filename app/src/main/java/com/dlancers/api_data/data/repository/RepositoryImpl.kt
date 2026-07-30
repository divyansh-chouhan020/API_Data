package com.dlancers.api_data.data.repository
import Product
import com.dlancers.api_data.data.remote.ProductApi
import com.dlancers.api_data.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val productApi :ProductApi
): ProductRepository{
     override suspend fun getProducts(): List<Product> {
        return productApi.getProducts();
    }
}