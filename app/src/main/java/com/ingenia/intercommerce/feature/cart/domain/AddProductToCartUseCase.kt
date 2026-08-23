package com.ingenia.intercommerce.feature.cart.domain

import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(product: CartItem) {
        repository.addOrUpdateItem(product)
    }
}
