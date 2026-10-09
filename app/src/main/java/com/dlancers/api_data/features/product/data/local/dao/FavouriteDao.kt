package com.dlancers.api_data.features.product.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dlancers.api_data.features.product.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Query("SELECT EXISTS(SELECT 1 FROM favourites WHERE uid = :uid)")
    fun isFavourite(uid: String): Flow<Boolean>

    @Query("SELECT uid FROM favourites")
    fun observeFavouriteUids(): Flow<List<String>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavourite(favourite: FavouriteEntity)

    @Delete
    suspend fun removeFavourite(favourite: FavouriteEntity)
}