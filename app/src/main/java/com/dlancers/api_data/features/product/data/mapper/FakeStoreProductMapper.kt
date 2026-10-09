package com.dlancers.api_data.features.product.data.mapper
import com.dlancers.api_data.features.product.data.models.FakeStoreProductDto
import com.dlancers.api_data.features.product.domain.models.Product

fun FakeStoreProductDto.toDomainProduct(): Product {
    return Product(
        uid = "fake_store_${id}",
        id = this.id,
        name = this.title,
        price = this.price,
        description = this.description,
        images = this.image?.let { listOf(it) } ?: emptyList()
    )
}