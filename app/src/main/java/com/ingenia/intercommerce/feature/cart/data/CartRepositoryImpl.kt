package com.ingenia.intercommerce.feature.cart.data

import com.ingenia.intercommerce.feature.cart.data.local.CartDao
import com.ingenia.intercommerce.feature.cart.data.local.toDomain
import com.ingenia.intercommerce.feature.cart.data.local.toEntity
import com.ingenia.intercommerce.feature.cart.domain.CartItem
import com.ingenia.intercommerce.feature.cart.domain.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItems().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addOrUpdateItem(item: CartItem) {
        val existingItem = cartDao.getCartItemById(item.productId)
        if (existingItem != null) {
            cartDao.updateQuantity(item.productId, existingItem.quantity + item.quantity)
        } else {
            cartDao.insertOrUpdateItem(item.toEntity())
        }
    }

    override suspend fun removeProduct(productId: Int) {
        cartDao.deleteItem(productId)
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteItem(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
