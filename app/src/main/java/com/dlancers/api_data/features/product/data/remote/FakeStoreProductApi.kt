package com.dlancers.api_data.features.product.data.remote

import com.dlancers.api_data.core.common.utils.Constants
import com.dlancers.api_data.features.product.data.models.FakeStoreProductDto
import retrofit2.http.GET
interface FakeStoreProductApi{

    @GET(Constants.PRODUCT_ENDPOINT)
    suspend fun getProducts(): List<FakeStoreProductDto>
}