package com.ingenia.intercommerce.feature.catalog.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<PagingData<Product>> {
        return repository.getProductsPaged()
    }
}
