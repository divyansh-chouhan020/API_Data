package com.dlancers.api_data.features.product.domain.usecase

import com.dlancers.api_data.features.product.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavouriteUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {

    suspend operator fun invoke(uid: String) {
        val isFavourite = favouriteRepository
            .isFavourite(uid)
            .first()

        if (isFavourite) {
            favouriteRepository.removeFavourite(uid)
        } else {
            favouriteRepository.addFavourite(uid)
        }
    }
}