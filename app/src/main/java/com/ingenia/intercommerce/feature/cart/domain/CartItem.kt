package com.ingenia.intercommerce.feature.cart.domain

data class CartItem(
    val productId: Int,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int
)
