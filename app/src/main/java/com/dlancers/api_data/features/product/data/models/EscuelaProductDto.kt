package com.dlancers.api_data.features.product.data.models

import com.google.gson.annotations.SerializedName

data class EscuelaProductDto (

    @SerializedName("id")
    val id :Int?,

    @SerializedName("title")
    val title :String?,

    @SerializedName("price")
    val price : Long?,

    @SerializedName("description")
    val description : String?,

    @SerializedName("images")
    val images : List<String>,
    )