package com.ingenia.intercommerce.feature.catalog.domain

import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): List<Product> {
        return repository.searchProducts(query)
    }
}
