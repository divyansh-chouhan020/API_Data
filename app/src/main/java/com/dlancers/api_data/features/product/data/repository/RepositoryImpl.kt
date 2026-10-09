package com.dlancers.api_data.features.product.data.repository
import com.dlancers.api_data.features.product.domain.models.Product
import com.dlancers.api_data.features.product.data.remote.EscuelaProductApi
import com.dlancers.api_data.features.product.data.remote.FakeStoreProductApi
import com.dlancers.api_data.features.product.domain.repository.ProductRepository
import com.dlancers.api_data.features.product.data.mapper.toDomainProduct
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ProductRepositoryImpl(
    private val productApi :EscuelaProductApi,
    private val fakeStoreProductApi : FakeStoreProductApi
): ProductRepository{
     override suspend fun getProducts(): List<Product> {
        return coroutineScope {
            // FIX: fetch each API independently; one failure must not cancel the other
            val escuelaProductsDeferred = async {
                runCatching {
                    productApi.getProducts().map { dto -> dto.toDomainProduct() }
                }.getOrElse { emptyList() }
            }

            val fakeStoreProductsDeferred = async {
                runCatching {
                    fakeStoreProductApi.getProducts().map { dto -> dto.toDomainProduct() }
                }.getOrElse { emptyList() }
            }

            val escuelaProducts = escuelaProductsDeferred.await()
            val fakeStoreProducts = fakeStoreProductsDeferred.await()
            val combinedProducts = escuelaProducts + fakeStoreProducts

            if (combinedProducts.isEmpty()) {
                throw IllegalStateException("Unable to load products from any source")
            }

            combinedProducts
        }
    }

}