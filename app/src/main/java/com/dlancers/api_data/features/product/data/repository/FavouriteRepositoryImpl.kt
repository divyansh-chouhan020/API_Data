package com.dlancers.api_data.features.product.data.repository

import com.dlancers.api_data.features.product.data.local.dao.FavouriteDao
import com.dlancers.api_data.features.product.data.local.entity.FavouriteEntity
import com.dlancers.api_data.features.product.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteDao: FavouriteDao
) : FavouriteRepository {

    override fun isFavourite(uid: String): Flow<Boolean> {
        return favouriteDao.isFavourite(uid)
    }

    override suspend fun addFavourite(uid: String) {
        favouriteDao.addFavourite(
            FavouriteEntity(uid = uid)
        )
    }

    override suspend fun removeFavourite(uid: String) {
        favouriteDao.removeFavourite(
            FavouriteEntity(uid = uid)
        )
    }
    override fun observeFavouriteUids(): Flow<Set<String>> {
        return favouriteDao.observeFavouriteUids().map { uids -> uids.toSet() }
    }
}