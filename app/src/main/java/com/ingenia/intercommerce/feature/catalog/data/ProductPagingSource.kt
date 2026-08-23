package com.ingenia.intercommerce.feature.catalog.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ingenia.intercommerce.core.data.remote.DummyJsonApi
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.data.local.ProductEntity
import com.ingenia.intercommerce.feature.catalog.data.local.toDomain
import com.ingenia.intercommerce.feature.catalog.domain.Product
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val api: DummyJsonApi,
    private val dao: ProductDao
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 0
        val limit = params.loadSize
        val skip = page * limit

        return try {
            // Intento de red
            val response = api.getProducts(limit = limit, skip = skip)
            val products = response.products.map { dto ->
                ProductEntity(
                    id = dto.id,
                    title = dto.title,
                    description = dto.description,
                    price = dto.price,
                    thumbnail = dto.thumbnail,
                    brand = dto.brand ?: "",
                    page = page
                )
            }
            // Guardar cache local
            dao.insertProducts(products)

            LoadResult.Page(
                data = products.map { it.toDomain() },
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            // Fallback Offline: Leer de Room
            val localProducts = dao.getProductsPaged(limit = limit, skip = skip)
            if (localProducts.isNotEmpty()) {
                LoadResult.Page(
                    data = localProducts.map { it.toDomain() },
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (localProducts.size < limit) null else page + 1
                )
            } else {
                LoadResult.Error(e) // No hay red ni cache
            }
        }
    }
}
