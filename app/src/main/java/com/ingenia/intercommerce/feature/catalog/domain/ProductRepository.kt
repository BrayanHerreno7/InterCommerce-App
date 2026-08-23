package com.ingenia.intercommerce.feature.catalog.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProductsPaged(): Flow<PagingData<Product>>
    suspend fun searchProducts(query: String): List<Product>
}
