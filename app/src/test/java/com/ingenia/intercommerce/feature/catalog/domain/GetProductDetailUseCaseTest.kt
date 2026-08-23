package com.ingenia.intercommerce.feature.catalog.domain

import com.ingenia.intercommerce.core.data.remote.DummyJsonApi
import com.ingenia.intercommerce.feature.catalog.data.remote.dto.ProductDto
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.data.local.ProductEntity
import com.ingenia.intercommerce.feature.productdetail.domain.GetProductDetailUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetProductDetailUseCaseTest {

    private lateinit var api: DummyJsonApi
    private lateinit var dao: ProductDao
    private lateinit var useCase: GetProductDetailUseCase

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk()
        useCase = GetProductDetailUseCase(api, dao)
    }

    @Test
    fun `invoke should return product from API when network is successful`() = runTest {
        // Arrange
        val productId = 1
        val apiResponse = ProductDto(
            id = productId,
            title = "Smartphone",
            description = "A great phone",
            price = 999.0,
            thumbnail = "url",
            brand = "TechBrand"
        )
        coEvery { api.getProductById(productId) } returns apiResponse

        // Act
        val result = useCase(productId)

        // Assert
        assertEquals("Smartphone", result?.title)
        assertEquals("TechBrand", result?.brand)
        coVerify(exactly = 1) { api.getProductById(productId) }
        coVerify(exactly = 0) { dao.getProductById(any()) }
    }

    @Test
    fun `invoke should fallback to DAO when network fails`() = runTest {
        // Arrange
        val productId = 1
        val localEntity = ProductEntity(
            id = productId,
            title = "Offline Phone",
            description = "A great phone",
            price = 999.0,
            thumbnail = "url",
            brand = "TechBrand", 
            page = 0
        )
        
        coEvery { api.getProductById(productId) } throws IOException("No internet")
        coEvery { dao.getProductById(productId) } returns localEntity

        // Act
        val result = useCase(productId)

        // Assert
        assertEquals("Offline Phone", result?.title)
        coVerify(exactly = 1) { api.getProductById(productId) }
        coVerify(exactly = 1) { dao.getProductById(productId) }
    }
}
