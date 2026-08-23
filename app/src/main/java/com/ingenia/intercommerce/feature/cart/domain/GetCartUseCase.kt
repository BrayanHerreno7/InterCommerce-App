package com.ingenia.intercommerce.feature.cart.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> = repository.getCartItems()
}
