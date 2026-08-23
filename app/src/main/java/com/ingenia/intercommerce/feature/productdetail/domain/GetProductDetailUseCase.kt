package com.ingenia.intercommerce.feature.productdetail.domain

import com.ingenia.intercommerce.core.data.remote.DummyJsonApi
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.data.local.toDomain
import com.ingenia.intercommerce.feature.catalog.domain.Product
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val api: DummyJsonApi,
    private val dao: ProductDao
) {
    suspend operator fun invoke(id: Int): Product? {
        return try {
            val dto = api.getProductById(id)
            Product(dto.id, dto.title, dto.description, dto.price, dto.thumbnail, dto.brand ?: "")
        } catch (e: Exception) {
            // Fallback to local DB
            dao.getProductById(id)?.toDomain()
        }
    }
}
