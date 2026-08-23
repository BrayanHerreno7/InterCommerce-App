package com.ingenia.intercommerce.feature.catalog.presentation

import androidx.paging.PagingData
import com.ingenia.intercommerce.MainDispatcherRule
import com.ingenia.intercommerce.feature.catalog.domain.GetProductsUseCase
import com.ingenia.intercommerce.feature.catalog.domain.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatalogViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var viewModel: CatalogViewModel

    @Before
    fun setUp() {
        getProductsUseCase = mockk()
    }

    @Test
    fun `viewModel should initialize productsPagingFlow`() = runTest {
        // Arrange
        val fakePagingData = PagingData.empty<Product>()
        coEvery { getProductsUseCase() } returns flowOf(fakePagingData)

        // Act
        viewModel = CatalogViewModel(getProductsUseCase)
        
        // Assert
        val result = viewModel.productsPagingFlow.first()
        assertNotNull(result)
    }
}

