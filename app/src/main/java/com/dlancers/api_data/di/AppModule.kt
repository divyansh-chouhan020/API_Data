package com.dlancers.api_data.di
import com.dlancers.api_data.data.remote.ProductApi
import com.dlancers.api_data.domain.repository.ProductRepository
import com.dlancers.api_data.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent:: class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(
            "https://api.escuelajs.co/api/v1/"
        ).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductApi(
        retrofit: Retrofit
    ): ProductApi{
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(
        productApi: ProductApi
    ):ProductRepository{
        return ProductRepositoryImpl(
            productApi
        )
    }
}