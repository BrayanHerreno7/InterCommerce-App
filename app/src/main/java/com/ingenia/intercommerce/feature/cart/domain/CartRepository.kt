package com.ingenia.intercommerce.feature.cart.domain

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addOrUpdateItem(item: CartItem)
    suspend fun removeProduct(productId: Int)
    suspend fun updateQuantity(productId: Int, quantity: Int)
    suspend fun clearCart()
}
