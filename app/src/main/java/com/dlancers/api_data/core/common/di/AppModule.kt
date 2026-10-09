package com.dlancers.api_data.core.common.di

import com.dlancers.api_data.BuildConfig
import com.dlancers.api_data.features.product.data.remote.EscuelaProductApi
import com.dlancers.api_data.features.product.data.remote.FakeStoreProductApi
import com.dlancers.api_data.features.product.data.repository.ProductRepositoryImpl
import com.dlancers.api_data.features.product.domain.repository.ProductRepository
import com.dlancers.api_data.features.product.domain.usecase.GetProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}
