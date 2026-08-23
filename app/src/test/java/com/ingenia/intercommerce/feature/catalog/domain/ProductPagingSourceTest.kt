package com.ingenia.intercommerce.feature.catalog.domain

import androidx.paging.PagingSource
import com.ingenia.intercommerce.core.data.remote.DummyJsonApi
import com.ingenia.intercommerce.feature.catalog.data.remote.dto.ProductDto
import com.ingenia.intercommerce.feature.catalog.data.remote.dto.ProductResponseDto
import com.ingenia.intercommerce.feature.catalog.data.ProductPagingSource
import com.ingenia.intercommerce.feature.catalog.data.local.ProductDao
import com.ingenia.intercommerce.feature.catalog.data.local.ProductEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ProductPagingSourceTest {

    private lateinit var api: DummyJsonApi
    private lateinit var dao: ProductDao
    private lateinit var pagingSource: ProductPagingSource

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk(relaxed = true)
        pagingSource = ProductPagingSource(api, dao)
    }

    @Test
    fun `load returns Page when network is successful`() = runTest {
        // Arrange
        val mockDto = ProductDto(id = 1, title = "Product 1", description = "Desc", price = 10.0, thumbnail = "url", brand = "Brand")
        val mockResponse = ProductResponseDto(
            products = listOf(mockDto),
            total = 1,
            skip = 0,
            limit = 10
        )
        coEvery { api.getProducts(limit = 10, skip = 0) } returns mockResponse

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals("Product 1", page.data[0].title)
        
        // Verifica que tambin guard en la DB local
        coVerify(exactly = 1) { dao.insertProducts(any()) }
    }

    @Test
    fun `load returns local data when network fails (Offline-First)`() = runTest {
        // Arrange
        val mockLocalEntity = ProductEntity(1, "Offline Product", "Desc", 20.0, "url", "Brand", 0)
        
        coEvery { api.getProducts(any(), any()) } throws IOException("No Internet")
        coEvery { dao.getProductsPaged(limit = 10, skip = 0) } returns listOf(mockLocalEntity)

        // Act
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        // Assert
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals("Offline Product", page.data[0].title)
    }
}

