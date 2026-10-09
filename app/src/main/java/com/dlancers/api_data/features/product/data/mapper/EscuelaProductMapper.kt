package com.dlancers.api_data.features.product.data.mapper

import com.dlancers.api_data.features.product.data.models.EscuelaProductDto
import com.dlancers.api_data.features.product.domain.models.Product

fun EscuelaProductDto.toDomainProduct(): Product {
    return Product(
        uid = "escuela_${id}",
        id = this.id,
        name = this.title,
        price = this.price?.toDouble(),
        description = this.description,
        images = this.images?.map { imageUrl ->
            imageUrl
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .trim()
        } ?: emptyList()
    )
}