package com.ingenia.intercommerce.feature.cart.presentation

import com.ingenia.intercommerce.MainDispatcherRule
import com.ingenia.intercommerce.feature.cart.domain.CartItem
import com.ingenia.intercommerce.feature.cart.domain.CartTotals
import com.ingenia.intercommerce.feature.cart.domain.CalculateCartTotalsUseCase
import com.ingenia.intercommerce.feature.cart.domain.GetCartUseCase
import com.ingenia.intercommerce.feature.cart.domain.CartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var cartRepository: CartRepository
    private lateinit var calculateCartTotalsUseCase: CalculateCartTotalsUseCase
    private lateinit var viewModel: CartViewModel

    @Before
    fun setUp() {
        getCartUseCase = mockk()
        cartRepository = mockk(relaxed = true)
        calculateCartTotalsUseCase = mockk()

        // Default mock behaviors
        coEvery { getCartUseCase() } returns flowOf(emptyList())
        coEvery { calculateCartTotalsUseCase(any()) } returns CartTotals(0.0, 0.0, 0.0)
    }

    @Test
    fun `viewModel should load cart items correctly on init`() = runTest {
        // Arrange
        val items = listOf(
            CartItem(1, "Product 1", 10.0, "url", 1),
            CartItem(2, "Product 2", 20.0, "url", 2)
        )
        val expectedTotals = CartTotals(50.0, 9.5, 59.5)

        coEvery { getCartUseCase() } returns flowOf(items)
        coEvery { calculateCartTotalsUseCase(items) } returns expectedTotals

        // Act
        viewModel = CartViewModel(
            getCartUseCase,
            calculateCartTotalsUseCase,
            cartRepository
        )
        val job = launch(kotlinx.coroutines.test.UnconfinedTestDispatcher()) { viewModel.cartTotals.collect { } }
        advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.cartItems.value.size)
        assertEquals(expectedTotals, viewModel.cartTotals.value)
        job.cancel()
    }

    @Test
    fun `removeItem should call use case`() = runTest {
        // Arrange
        viewModel = CartViewModel(
            getCartUseCase,
            calculateCartTotalsUseCase,
            cartRepository
        )

        // Act
        viewModel.removeItem(1)
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { cartRepository.removeProduct(1) }
    }
}



