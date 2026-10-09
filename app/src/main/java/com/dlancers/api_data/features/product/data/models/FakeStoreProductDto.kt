package com.dlancers.api_data.features.product.data.models

import com.google.gson.annotations.SerializedName

data class FakeStoreProductDto(

    @SerializedName("id")
    val id : Int?,

    @SerializedName("title")
    val title : String?,

    @SerializedName("price")
    val price : Double?,

    @SerializedName("description")
    val description : String?,

    @SerializedName("image")
    val image : String?
)

