package com.dlancers.api_data.features.product.domain.usecase

import com.dlancers.api_data.features.product.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavouriteUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {

    operator fun invoke(): Flow<Set<String>> {
        return favouriteRepository.observeFavouriteUids()
    }
}