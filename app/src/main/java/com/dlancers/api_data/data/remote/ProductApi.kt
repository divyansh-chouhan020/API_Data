package com.dlancers.api_data.data.remote
import Product

import retrofit2.http.GET
interface ProductApi{
    @GET(Constants.PRODUCT_ENDPOINT)
    suspend fun getProducts (): List<Product>
}