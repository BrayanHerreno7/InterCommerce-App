package com.ingenia.intercommerce.feature.cart.domain

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculateCartTotalsUseCaseTest {

    private lateinit var useCase: CalculateCartTotalsUseCase

    @Before
    fun setUp() {
        useCase = CalculateCartTotalsUseCase()
    }

    @Test
    fun `invoke should calculate correct totals for multiple items`() {
        // Arrange
        val items = listOf(
            CartItem(1, "Product 1", 100.0, "url", 2), // 200.0
            CartItem(2, "Product 2", 50.0, "url", 1)   // 50.0
        )
        // Subtotal esperado: 250.0
        // Impuesto (19%): 47.5
        // Total: 297.5

        // Act
        val result = useCase(items)

        // Assert
        assertEquals(250.0, result.subtotal, 0.001)
        assertEquals(47.5, result.tax, 0.001)
        assertEquals(297.5, result.total, 0.001)
    }

    @Test
    fun `invoke should return zeros when cart is empty`() {
        // Arrange
        val items = emptyList<CartItem>()

        // Act
        val result = useCase(items)

        // Assert
        assertEquals(0.0, result.subtotal, 0.0)
        assertEquals(0.0, result.tax, 0.0)
        assertEquals(0.0, result.total, 0.0)
    }
}
