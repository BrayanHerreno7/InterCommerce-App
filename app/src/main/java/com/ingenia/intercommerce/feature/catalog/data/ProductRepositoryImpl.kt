package com.ingenia.intercommerce.feature.catalog.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ingenia.intercommerce.core.data.remote.DummyJsonApi
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.data.local.toDomain
import com.ingenia.intercommerce.feature.catalog.domain.Product
import com.ingenia.intercommerce.feature.catalog.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: DummyJsonApi,
    private val dao: ProductDao
) : ProductRepository {

    override fun getProductsPaged(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPagingSource(api, dao) }
        ).flow
    }

    override suspend fun searchProducts(query: String): List<Product> {
        return try {
            val response = api.searchProducts(query)
            response.products.map { dto ->
                Product(dto.id, dto.title, dto.description, dto.price, dto.thumbnail, dto.brand ?: "")
            }
        } catch (e: Exception) {
            dao.searchProducts(query).map { it.toDomain() }
        }
    }
}
