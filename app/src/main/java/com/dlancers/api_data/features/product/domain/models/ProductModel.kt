package com.dlancers.api_data.features.product.domain.models
data class Product (
    val uid : String,
    val id :Int?,
    val name : String?,
    val price : Double? ,
    val description :String?,
    val images : List<String>?
)