package com.dlancers.api_data.features.product.data.remote

import com.dlancers.api_data.core.common.utils.Constants
import com.dlancers.api_data.features.product.data.models.EscuelaProductDto
import retrofit2.http.GET
interface EscuelaProductApi{
    @GET(Constants.PRODUCT_ENDPOINT)
    suspend fun getProducts (): List<EscuelaProductDto>

}