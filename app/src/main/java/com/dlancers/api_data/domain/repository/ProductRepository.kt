package com.dlancers.api_data.domain.repository

import Product

interface ProductRepository {
   suspend  fun getProducts(): List<Product>
}

