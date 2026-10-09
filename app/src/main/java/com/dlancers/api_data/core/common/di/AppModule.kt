package com.dlancers.api_data.core.common.di

import com.dlancers.api_data.BuildConfig
import com.dlancers.api_data.features.product.data.remote.EscuelaProductApi
import com.dlancers.api_data.features.product.data.remote.FakeStoreProductApi
import com.dlancers.api_data.features.product.data.repository.ProductRepositoryImpl
import com.dlancers.api_data.features.product.domain.repository.ProductRepository
import com.dlancers.api_data.features.product.domain.usecase.GetProductsUseCase
import com.dlancers.api_data.features.location.data.datasource.AndroidLocationDataSource
import com.dlancers.api_data.features.location.data.datasource.LocationDataSource
import com.dlancers.api_data.features.location.data.repository.LocationRepositoryImpl
import com.dlancers.api_data.features.location.domain.repository.LocationRepository
import com.dlancers.api_data.features.location.domain.usecase.GetCurrentLocationUseCase
import com.dlancers.api_data.features.notification.data.datasource.AndroidNotificationDataSource
import com.dlancers.api_data.features.notification.data.datasource.NotificationDataSource
import com.dlancers.api_data.features.notification.data.repository.NotificationRepositoryImpl
import com.dlancers.api_data.features.notification.domain.repository.NotificationRepository
import com.dlancers.api_data.features.notification.domain.usecase.ShowDemoNotificationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.dlancers.api_data.core.database.AppDatabase
import com.dlancers.api_data.features.product.data.local.dao.FavouriteDao
import com.dlancers.api_data.features.product.data.repository.FavouriteRepositoryImpl
import com.dlancers.api_data.features.product.domain.repository.FavouriteRepository
import androidx.room.Room
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent:: class)
object AppModule {

    @Provides
    @Singleton
    // FIX: return type was invalid `okHttpClient`; must be OkHttpClient
    fun provideOkHttpClient(): OkHttpClient {
        val logginginterceptor = HttpLoggingInterceptor().apply{
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().addInterceptor(logginginterceptor)
            .build()
    }


    // 1. Make the retrofit instance
    @Provides
    @Singleton
    @Named("EscuelaJsRetrofit")
    // FIX: inject OkHttpClient instead of referencing undefined `okHttpClient`
    fun provideEscuelaJsRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("FakeStoreRetrofit")
    // FIX: inject OkHttpClient instead of referencing undefined `okHttpClient`
    fun provideFakeStoreRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BuildConfig.FAKESTORE_BASE_URL)
            .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
    }

    // 2. Now make the API classes
    @Provides
    @Singleton
    fun provideProductApi(
        @Named("EscuelaJsRetrofit") retrofit: Retrofit
    ): EscuelaProductApi{
        return retrofit.create(EscuelaProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFakeStoreProductApi(
        @Named("FakeStoreRetrofit") retrofit: Retrofit
    ): FakeStoreProductApi{
        return retrofit.create(FakeStoreProductApi::class.java)
    }

    // 3. Last return the Product Repository
    @Provides
    @Singleton
    fun provideRepository(
        productApi: EscuelaProductApi,
        fakeStoreProductApi: FakeStoreProductApi
    ): ProductRepository {
        return ProductRepositoryImpl(
            productApi = productApi ,
            fakeStoreProductApi = fakeStoreProductApi
        )
    }

    @Provides
    fun provideGetProductsUseCase(
        productRepository: ProductRepository,
    ): GetProductsUseCase {
        return GetProductsUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context,
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    // for  the permission related things
    // 1. Datasource 2. Repository and 3. Usecase functions should be provided in the appModule
    @Provides
    @Singleton
    fun provideLocationDataSource(
        androidLocationDataSource: AndroidLocationDataSource,
    ): LocationDataSource {
        return androidLocationDataSource
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl,
    ): LocationRepository {
        return locationRepositoryImpl
    }

    @Provides
    fun provideGetCurrentLocationUseCase(
        locationRepository: LocationRepository,
    ): GetCurrentLocationUseCase {
        return GetCurrentLocationUseCase(locationRepository)
    }

    // For the Notification task

    @Provides
    @Singleton
    fun provideNotificationDataSource(
        androidNotificationDataSource: AndroidNotificationDataSource,
    ): NotificationDataSource {
        return androidNotificationDataSource
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl,
    ): NotificationRepository {
        return notificationRepositoryImpl
    }

    @Provides
    fun provideShowDemoNotificationUseCase(
        notificationRepository: NotificationRepository,
    ): ShowDemoNotificationUseCase {
        return ShowDemoNotificationUseCase(notificationRepository)
    }

    // Adding room Database Singleton
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "api_data_database"
        ).build()
    }

    @Provides
    fun provideFavouriteDao(
        database: AppDatabase
    ): FavouriteDao {
        return database.favouriteDao()
    }

    @Provides
    @Singleton
    fun provideFavouriteRepository(
        favouriteRepositoryImpl: FavouriteRepositoryImpl
    ): FavouriteRepository {
        return favouriteRepositoryImpl
    }
}
// For fetching the API data

// Hilt do three works
// 1. Create a Retrofit instance
// 2. Provide the API class
// 3. Return the Product Repository