package com.dlancers.api_data.features.product.domain.usecase

import com.dlancers.api_data.features.product.domain.models.Product
import com.dlancers.api_data.features.product.domain.repository.ProductRepository

class GetProductsUseCase(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(): List<Product> {
        return productRepository.getProducts()
    }
}
