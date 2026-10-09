package com.dlancers.api_data.core.common.di

import android.content.Context
import com.dlancers.api_data.BuildConfig
import com.dlancers.api_data.features.location.data.datasource.AndroidLocationDataSource
import com.dlancers.api_data.features.location.data.datasource.LocationDataSource
import com.dlancers.api_data.features.location.data.repository.LocationRepositoryImpl
import com.dlancers.api_data.features.location.domain.repository.LocationRepository
import com.dlancers.api_data.features.location.domain.usecase.GetCurrentLocationUseCase
import com.dlancers.api_data.features.product.data.remote.EscuelaProductApi
import com.dlancers.api_data.features.product.data.remote.FakeStoreProductApi
import com.dlancers.api_data.features.product.data.repository.ProductRepositoryImpl
import com.dlancers.api_data.features.product.domain.repository.ProductRepository
import com.dlancers.api_data.features.product.domain.usecase.GetProductsUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("EscuelaJsRetrofit")
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
    fun provideFakeStoreRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.FAKESTORE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApi(
        @Named("EscuelaJsRetrofit") retrofit: Retrofit,
    ): EscuelaProductApi {
        return retrofit.create(EscuelaProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFakeStoreProductApi(
        @Named("FakeStoreRetrofit") retrofit: Retrofit,
    ): FakeStoreProductApi {
        return retrofit.create(FakeStoreProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        productApi: EscuelaProductApi,
        fakeStoreProductApi: FakeStoreProductApi,
    ): ProductRepository {
        return ProductRepositoryImpl(
            productApi = productApi,
            fakeStoreProductApi = fakeStoreProductApi,
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
}
