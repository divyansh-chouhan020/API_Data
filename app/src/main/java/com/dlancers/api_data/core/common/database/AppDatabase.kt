package com.dlancers.api_data.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dlancers.api_data.features.product.data.local.dao.FavouriteDao
import com.dlancers.api_data.features.product.data.local.entity.FavouriteEntity

@Database(
    entities = [FavouriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteDao(): FavouriteDao
}