package com.dlancers.api_data.features.product.domain.repository

import kotlinx.coroutines.flow.Flow
interface FavouriteRepository {

    fun isFavourite(uid: String): Flow<Boolean>

    suspend fun addFavourite(uid: String)

    suspend fun removeFavourite(uid: String)

    fun observeFavouriteUids(): Flow<Set<String>>
}