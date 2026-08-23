package com.ingenia.intercommerce.feature.cart.domain

data class CartTotals(
    val subtotal: Double,
    val tax: Double,
    val total: Double
)
